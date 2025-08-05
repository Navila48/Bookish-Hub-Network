import { Component } from '@angular/core';
import {AuthenticationRequest} from '../../services/models/authentication-request';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';
import {TokenService} from '../../services/token/token-service';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  authRequest : AuthenticationRequest = {email : '', password : '' };
  errorMsg : Array<String> = [];

constructor(
  private router : Router,
  private authService : AuthenticationService,
  private tokenService : TokenService
) {}

  login() {
    this.errorMsg = [];
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: (res):void =>{
        this.tokenService.set(res.token as string);
        this.router.navigate(['books']);
      },
      error: (err) =>{
        console.log(err);

        if(err.error.validationErrors){
          this.errorMsg = err.error.validationErrors;
        }else{
          this.errorMsg.push(err.error.error);
        }
      }
    });
  }

  registerUser() {
    this.router.navigate(['register'])
  }
}
