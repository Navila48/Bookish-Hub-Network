package com.tasnuva.book.auth;

import com.tasnuva.book.email.EmailService;
import com.tasnuva.book.email.EmailTemplateName;
import com.tasnuva.book.role.RoleRepository;
import com.tasnuva.book.security.JwtService;
import com.tasnuva.book.user.Token;
import com.tasnuva.book.user.TokenRepository;
import com.tasnuva.book.user.User;
import com.tasnuva.book.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(()-> new IllegalStateException("Role is not found"));

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateToken(user);
        emailService.sendEmail(user.getEmail(), user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,activationUrl,newToken,"Account Activation" );
    }

    private String generateToken(User user) {
        String generateToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generateToken)
                .createdAt(LocalDateTime.now() )
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generateToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder activationCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomChar = random.nextInt(characters.length());
            activationCode.append(characters.charAt(randomChar));
        }
        return activationCode.toString();
    }

    public AuthenticationResponse authenticate(@Valid AuthenticationRequest authenRequest) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenRequest.getEmail(),
                        authenRequest.getPassword()
                )
        );
        var claims = new HashMap<String,Object>();
        var user = (User) auth.getPrincipal();
        claims.put("fullName", user.fullName());
        String jwtToken = jwtService.generateToken(claims,user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(()-> new IllegalStateException("Token is not found"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(()-> new IllegalStateException("User is not found"));

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
