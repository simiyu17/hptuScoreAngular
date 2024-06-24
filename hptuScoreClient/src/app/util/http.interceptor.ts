import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

export const httpInterceptor: HttpInterceptorFn = (req, next) => {
  const token: string = localStorage.getItem("auth_token")!;
  const authService: AuthService = inject(AuthService)
  const forceChangePassword: string = localStorage.getItem(authService.FORCE_PASS_CHANGE)!;
 
  if(forceChangePassword && 'true' == forceChangePassword){
    authService.redirectToChangePassword()
  }
  if (token != null) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' }
    });
  }else {
    req = req.clone({ headers: req.headers.set('Content-Type', 'application/json') });
  }
  req = req.clone({ headers: req.headers.set('Accept', 'application/json') });

  return next(req);
};
