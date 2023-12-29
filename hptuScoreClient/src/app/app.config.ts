import { APP_INITIALIZER, ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { KeycloakService } from 'keycloak-angular';
import { initializeKeycloak } from './util/KeyCloakInitializer';
import { AuthService } from './services/auth.service';
import { httpInterceptorProvider } from './util/TokenInterceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes), 
    provideAnimations(), 
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    }, 
    KeycloakService,
    AuthService,
    httpInterceptorProvider
  ]
};
