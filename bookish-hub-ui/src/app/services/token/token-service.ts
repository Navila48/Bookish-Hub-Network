import { Injectable } from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  set(token: string){
    localStorage.setItem('token', token);
  }

  get(){
    if (typeof window === 'undefined') return null;
    return localStorage.getItem('token') as string;
  }

  isTokenValid() {
    const token = this.get();
    if(!token){
      return false;
    }
    //decode token
    const jwtHelper = new JwtHelperService();
    //check expired date
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if(isTokenExpired){
      localStorage.clear();
      return false;
    }
    return true;
  }

   getUsername(){
      const token = this.get();
      if(!token) return "Guest";
      const payload = token.split('.')[1];
      console.log("Payload ",payload);
      const decodedToken = JSON.parse(atob(payload));
      console.log("Decoded Token ",decodedToken);
      return decodedToken.fullName;
  }
}
