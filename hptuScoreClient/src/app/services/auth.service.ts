import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile, KeycloakTokenParsed } from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient, private globalService: GlobalService, private keycloakService: KeycloakService) { }

  /*login(username: string, password: string) {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password)
      .set('grant_type', 'password')
      .set('client_id', this.globalService.KEY_CLOAK_CLIENT_ID);
  
    return this.httpClient.post(`${this.globalService.BASE_KEY_CLOAK_URL}/realms/your-realm/protocol/openid-connect/token`, body.toString(), {
      headers: new HttpHeaders()
        .set('Content-Type', 'application/x-www-form-urlencoded')
    });
  }*/

  public setAuthToken(token : string): void {
      localStorage.setItem('auth_token', token)
  }

  public getAuthToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  public removeAuthToken(): void {
    localStorage.removeItem('auth_token');
  }

  public getLoggedUser(): KeycloakTokenParsed | undefined {
    try {
      const userDetails: KeycloakTokenParsed | undefined = this.keycloakService.getKeycloakInstance()
        .idTokenParsed;
      return userDetails;
    } catch (e) {
      console.error("Exception", e);
      return undefined;
    }
  }

  public isLoggedIn() : boolean {
    return this.keycloakService.isLoggedIn();
  }

  public loadUserProfile() : Promise<KeycloakProfile> {
    return this.keycloakService.loadUserProfile();
  }

  public login() : void {
    this.keycloakService.login();
  }

  public logout() : void {
    this.removeAuthToken();
    this.keycloakService.logout(window.location.origin);
  }

  public redirectToProfile(): void {
    this.keycloakService.getKeycloakInstance().accountManagement();
  }

  public redirectToChangePassword() {
    window.location.href = `${this.globalService.BASE_KEY_CLOAK_URL}/realms/${this.globalService.KEY_CLOAK_REALM}/protocol/openid-connect/auth?client_id=${this.globalService.KEY_CLOAK_CLIENT_ID}&redirect_uri=${window.location.origin}&response_type=code&scope=openid&kc_action=UPDATE_PASSWORD`;
  }

  public getRoles(): string[] {
    return this.keycloakService.getUserRoles();
  }
}
