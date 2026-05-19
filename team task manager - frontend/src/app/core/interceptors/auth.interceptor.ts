import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const headers: Record<string, string> = {};

  if (req.url.includes('loca.lt')) {
    headers['Bypass-Tunnel-Reminder'] = 'true';
  }

  const token = localStorage.getItem('token');
  if (token && !req.url.includes('/auth/')) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  if (Object.keys(headers).length > 0) {
    req = req.clone({ setHeaders: headers });
  }

  return next(req);
};
