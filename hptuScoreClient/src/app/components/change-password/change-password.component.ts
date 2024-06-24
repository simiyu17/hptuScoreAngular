import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../services/user.service';
import { GlobalService } from '../../services/global.service';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule
  ],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.scss'
})
export class ChangePasswordComponent {

  userPassChangeForm: FormGroup = this.fb.group({});
  authResponse?: { success: boolean, message: string, authToken: string }
  constructor(private fb: FormBuilder, private userSevice: UserService, private gs: GlobalService, private router: Router, private authService: AuthService) {
    this.createUserPassChangeForm();
  }


  createUserPassChangeForm(): void {
    this.userPassChangeForm = this.fb.group({
      password: [null, Validators.required],
      newPass: [null, Validators.required],
      passConfirm: [null, Validators.required]
    });

  }


  onUserPassChangeSubmit(): void {
    this.userSevice.updateUserPassword(this.userPassChangeForm.value).subscribe({
      next: (response) => {
        this.authResponse = response
        this.gs.openSnackBar("Password changed sucessfully!!", "Dismiss");
        this.authService.doLogout();

      }, error: (error: HttpErrorResponse) => {
          this.gs.openSnackBar(`An error occured: ${error.error.detail}`, "Dismiss");     
      }
    });
  }

}
