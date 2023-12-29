import { HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { KeycloakService } from "keycloak-angular";
import { Observable } from "rxjs";

@Injectable()
export class TokenInterceptor implements HttpInterceptor
{
  constructor(private kcService: KeycloakService)
  {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>
  {
    const authToken = this.kcService.getToken() || "";
    request = request.clone({
      setHeaders: {
        "Authorization": "Bearer " + authToken
      }
    });
    request = request.clone({ headers: request.headers.set('Accept', 'application/json') });
    request = request.clone({ headers: request.headers.set('Content-Type', 'application/json') });
    return next.handle(request);
  }
}

export const httpInterceptorProvider = 
    {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true}
