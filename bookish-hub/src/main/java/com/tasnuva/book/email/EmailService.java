package com.tasnuva.book.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class EmailService {

    // Injected by Spring, used to send emails
    private final JavaMailSender mailSender;
    // Injected by Spring, used to process email templates (e.g., Thymeleaf)
    private final SpringTemplateEngine templateEngine;

/**
 * Sends an email using a template.
 * The @Async annotation means this method will be executed in a separate thread,
 * non-blocking the calling code.
 * */
    @Async
    public void sendEmail(String to, String userName,EmailTemplateName emailTemplateName,
                          String confirmationUrl, String activationCode, String subject) throws MessagingException {
        String templateName;
        if(emailTemplateName != null) {
            templateName = emailTemplateName.name();
        }else{
            templateName = "confirm-email";
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // Helper class to easily construct the MIME message
        // MULTIPART_MODE_MIXED allows for both text and HTML parts
        // StandardCharsets.UTF_8 ensures proper character encoding
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("username", userName);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        // Process the email template using the template engine and the context
        // This generates the final HTML content of the email
        String html = templateEngine.process(templateName, context);

        // Configure the MimeMessageHelper with email details
        mimeMessageHelper.setFrom("tahichy48@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(html, true);

        // Send the constructed MIME message using the mail sender
        mailSender.send(mimeMessage);
    }
}
