import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { HeaderComponent } from './components/shared/header/header.component';
import { SidebarComponent } from './components/shared/sidebar/sidebar.component';
import { FooterComponent } from './components/shared/footer/footer.component';
import { RouterOutlet } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    CommonModule, 
    LoginComponent,
    HomeComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit{
  isUserLoggedIn: boolean = false;

  constructor(private authService: AuthService) { }

  public userName = "";
  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.isUserLoggedIn = true;
    }else {
      this.authService.doLogout()
    }
  }
}
