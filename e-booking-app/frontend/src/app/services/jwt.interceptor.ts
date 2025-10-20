import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Skip adding token for auth endpoints
    if (request.url.includes('/api/auth/')) {
      return next.handle(request);
    }

    const token = this.authService.getToken();

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        // Check if account is blocked
        if (error.status === 403 && error.error?.error === 'ACCOUNT_BLOCKED') {
          // Logout user immediately
          this.authService.logout();

          // Redirect to login with blocked account message
          this.router.navigate(['/login'], {
            queryParams: { blocked: 'true' }
          });

          alert('Votre compte a été bloqué par l\'administrateur. Veuillez contacter le support client.');
        }

        return throwError(() => error);
      })
    );
  }
}
