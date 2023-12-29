import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { KeycloakProfile } from 'keycloak-js';
import { KeycloakService } from 'keycloak-angular';
import { SharedModule } from './modules/shared/shared.module';
import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HomeComponent, LoginComponent, SharedModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'hptuScoreClient';
  isUserLoggedIn: boolean = false;
  isUser = false;
  isAdmin = false;

  public profile: KeycloakProfile | null = null;

  constructor(private readonly keycloakService: KeycloakService, private authService: AuthService) { 
    
  }

  public userName = "";
  async ngOnInit() {
    
    this.isUserLoggedIn = this.keycloakService.isLoggedIn();

    if (this.isUserLoggedIn) {
      const roles = this.keycloakService.getUserRoles();
      this.isUser = roles.includes('user');
      this.isAdmin = roles.includes('admin');
      this.profile = await this.keycloakService.loadUserProfile();
      this.userName += this.profile.username;
    }else {
      this.keycloakService.getKeycloakInstance().login()
    }
  }

  login() {
    this.authService.login();
  }

  logout() {
    this.authService.logout();
  }
}
