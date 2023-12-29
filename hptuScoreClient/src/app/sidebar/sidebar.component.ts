import { Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink, 
    MatIconModule, 
    RouterLinkActive
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent implements OnInit {
  isUserAdmin: boolean = false

  constructor(private authService: AuthService){}

  ngOnInit(): void {
    this.isUserAdmin = this.authService.getRoles().includes('admin')
  }
}
