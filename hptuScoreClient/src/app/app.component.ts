import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { filter, map } from 'rxjs';


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
export class AppComponent {
  isUserLoggedIn: boolean = false;

  constructor(private authService: AuthService, private router: Router){

      this.router.events.subscribe((event: any) => {
          this.navigationInterceptor(event);
      })
  }

  private navigationInterceptor(event: Event): void {
    if (event instanceof NavigationEnd) {
      this.isUserLoggedIn = this.authService.isAuthenticated();
    }
   
  }

}
