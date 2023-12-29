import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { KeycloakProfile } from 'keycloak-js';
import { KeycloakService } from 'keycloak-angular';
import { Component, EventEmitter, Output } from '@angular/core';
import { AuthService } from './services/auth.service';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule, 
    HomeComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  isUserLoggedIn: boolean = false;

  public profile: KeycloakProfile | null = null;

  constructor(private keycloakService: KeycloakService, private authService: AuthService) { }

  public userName = "";
  async ngOnInit() {
    if (this.keycloakService.isLoggedIn() && !this.keycloakService.isTokenExpired()) {
      this.authService.setAuthToken(await this.keycloakService.getToken());
      this.profile = await this.keycloakService.loadUserProfile();
      this.userName += this.profile.username;
    }else {
      this.authService.login()
    }
  }
}
