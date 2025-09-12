import {HttpHeaders, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from '@angular/core';
import {TokenService} from '../token/token-service';

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const token = tokenService.get();
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
