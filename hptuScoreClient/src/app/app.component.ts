import { CommonModule } from '@angular/common';
import { KeycloakProfile } from 'keycloak-js';
import { KeycloakService } from 'keycloak-angular';
import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';
import { HeaderComponent } from './components/shared/header/header.component';
import { SidebarComponent } from './components/shared/sidebar/sidebar.component';
import { FooterComponent } from './components/shared/footer/footer.component';
import { RouterOutlet } from '@angular/router';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    CommonModule, 
    HeaderComponent, 
    SidebarComponent, 
    FooterComponent
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
