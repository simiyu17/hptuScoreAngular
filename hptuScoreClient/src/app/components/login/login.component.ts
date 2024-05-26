import { Component, OnInit } from '@angular/core';
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
export class LoginComponent implements OnInit {

  userloginForm: FormGroup = this.fb.group({});
  msg: string = "";
  authResponse?: { success: boolean, message: string, authToken: string }
  constructor(private fb: FormBuilder, private us: UserService, private gs: GlobalService, private router: Router, private authService: AuthService) {
    this.createUserloginForm();
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
        console.log(error.error)
        if(error.status != 401 && error.status != 403){
          this.gs.openSnackBar("No connection to server ", "Dismiss");
        } else {
          this.gs.openSnackBar(`An error occured: ${error.error.detail}`, "Dismiss");
        }        
      }
    });
  }

  ngOnInit(): void {
    this.authService.userRedirection();
  }
}
