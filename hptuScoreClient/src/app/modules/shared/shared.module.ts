import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { KeycloakAngularModule, KeycloakBearerInterceptor, KeycloakService } from 'keycloak-angular';
import { PillarsService } from '../../services/pillars.service';
import { AuthService } from '../../services/auth.service';
import { httpInterceptorProvider } from '../../util/TokenInterceptor';

@NgModule({
  declarations: [],
  imports: [
    CommonModule, HttpClientModule, ReactiveFormsModule, FormsModule, KeycloakAngularModule
  ],
  exports:[HttpClientModule, ReactiveFormsModule, FormsModule, KeycloakAngularModule],
  providers: [
    PillarsService, 
    AuthService, 
    KeycloakService,
    httpInterceptorProvider,
  ]
})
export class SharedModule { }
