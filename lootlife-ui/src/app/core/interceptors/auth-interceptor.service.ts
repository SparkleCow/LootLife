import { HttpEvent, HttpHandler, HttpInterceptor, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptorFn: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
 const excludedUrls = [
    '/auth/login',
    '/auth/register'
  ];

  const isExcluded = excludedUrls.some(url => req.url.endsWith(url) || req.url.includes(url));

  if (isExcluded) {
    return next(req);
  }

  const authReq = token
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      })
    : req;

  return next(authReq);
};
