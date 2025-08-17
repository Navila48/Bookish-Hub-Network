import { Component } from '@angular/core';
import {CodeInputModule} from 'angular-code-input';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';

@Component({
  selector: 'app-activate-account',
  imports: [
    CodeInputModule
  ],
  templateUrl: './activate-account.html',
  styleUrl: './activate-account.scss'
})
export class ActivateAccount {
  message = '';
  isOkay = true;
  isSubmitted = false;
  constructor(private router: Router, private authService: AuthenticationService) {
  }

  onCodeCompleter(activationCode: string) {
    this.confirmAccount(activationCode);
  }

  private confirmAccount(token: string) {
    this.message = '';
    this.authService.confirm({
      token
    }).subscribe({
      next:()=>{
        this.message = "Your account has been successfully activated.\nNow you can proceed to login";
        this.isSubmitted = true
        this.isOkay = true
      },
      error: ()=>{
        this.message = "Your activation code has been expired or invalid"
        this.isSubmitted = true;
        this.isOkay = false;
      }
    })
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }
}
