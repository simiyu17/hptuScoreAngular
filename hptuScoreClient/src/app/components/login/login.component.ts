import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { GlobalService } from '../../services/global.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements AfterViewInit {

  userloginForm: FormGroup = this.fb.group({});
  invalidLogin = false;
  msg: string = "";
  authResponse?: {success: boolean, message: string, authToken: string}
  @ViewChild('usernameRef') usernameElementRef: ElementRef = {} as ElementRef;
  constructor(private fb: FormBuilder, private us: UserService, private gs: GlobalService, private router: Router, private authService: AuthService) {
    this.createUserloginForm();
  }

  ngAfterViewInit(): void {
    this.usernameElementRef.nativeElement.focus();
  }

  createUserloginForm(): void {
    this.userloginForm = this.fb.group({
      username: [null, Validators.email],
      password: [null, Validators.required]
    });
  }

  onUserLoginSubmit(): void {
    this.authService.login(this.userloginForm.value).subscribe({
      next: (response) => {
        this.authResponse = response
        this.authService.storeUserDetails(this.authResponse?.authToken);
        this.authService.userRedirection();
        
      }, error: (error: HttpErrorResponse) => {
        this.invalidLogin = true;
      }
    });
  }

  ngOnInit(): void {
    this.authService.userRedirection();
  }
}
