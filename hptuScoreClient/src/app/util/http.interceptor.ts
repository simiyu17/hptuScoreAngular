import { HttpInterceptorFn } from '@angular/common/http';

export const httpInterceptor: HttpInterceptorFn = (req, next) => {
  const token: string = localStorage.getItem("auth_token")!;
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
