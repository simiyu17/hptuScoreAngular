import { KeycloakService } from "keycloak-angular";

export function initializeKeycloak(keycloak: KeycloakService) {
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