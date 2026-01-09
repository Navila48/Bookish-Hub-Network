import {inject, Injectable, PLATFORM_ID} from '@angular/core';
import Keycloak from 'keycloak-js';
import {isPlatformBrowser} from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak : Keycloak | undefined;
  private platformId = inject(PLATFORM_ID);
  get keycloak(){
    if(!this._keycloak && isPlatformBrowser(this.platformId)){
      this._keycloak = new Keycloak(
        {
          'url': 'http://localhost:9090',
          'realm': 'Bookhub',
          'clientId': 'BookhubClient'
        }
      );
    }
    return this._keycloak;
  }
  async init(){
    if (!isPlatformBrowser(this.platformId)) {
      console.log("Skipping authentication - not in browser");

    }
    console.log("Authenticating user....");
    const authenticated = await this.keycloak?.init({
      onLoad: 'login-required'
    })
    if(authenticated){
      console.log("User authenticated");
    }
  }

  login(){
    return this.keycloak?.login();
  }

  logout(){
    return this.keycloak?.logout({redirectUri:'http://localhost:4200'});
  }
}
