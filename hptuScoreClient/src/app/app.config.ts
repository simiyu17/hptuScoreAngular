import { APP_INITIALIZER, ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { KeycloakService } from 'keycloak-angular';
import { AuthService } from './services/auth.service';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { httpInterceptor } from './util/http.interceptor';

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8081/auth',
        realm: 'hptu-score-realm',
        clientId: 'front-end-client-id',
      },
      initOptions: {
          onLoad: 'login-required',
          flow: 'standard'
        },
        enableBearerInterceptor: true
    });
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes), 
    provideAnimations(), 
    provideHttpClient(withInterceptors([httpInterceptor])),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    }, 
    KeycloakService,
    AuthService
  ]
};
