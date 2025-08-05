import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  set(token: string){
    localStorage.setItem('token', token);
  }

  get(){
    return localStorage.getItem('token') as string;
  }
}
