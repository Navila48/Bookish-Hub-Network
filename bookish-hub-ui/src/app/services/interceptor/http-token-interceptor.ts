import {HttpHeaders, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from '@angular/core';
import {KeycloakService} from '../keycloak/keycloakService';

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(KeycloakService);
  const token = keycloakService.keycloak?.token;
  if(token){
    const authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
    });
    return next(authReq);
  }
  return next(req);
};
