import { Component } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})
export class UsersComponent {

  constructor(private readonly keycloak: KeycloakService) { }

  public hasAdminRole: boolean = false;

  ngOnInit(): void {
    this.hasAdminRole = this.keycloak.getUserRoles().includes('admin');
   } 
}
