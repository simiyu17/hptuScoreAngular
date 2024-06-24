import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
import { GlobalService } from '../../../services/global.service';
import { UserDto } from '../../../dto/UserDto';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';

@Component({
  selector: 'app-edit-user',
  standalone: true,
  imports: [
    ReactiveFormsModule, 
    FormsModule, 
    MatInputModule,
    MatFormFieldModule,
    MatDialogModule,
    MatButtonModule,
    MatSelectModule,
    MatOptionModule
  ],
  templateUrl: './edit-user.component.html',
  styleUrl: './edit-user.component.scss'
})
export class EditUserComponent {

  createUserForm: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, 
    private userService: UserService, 
    private router: Router, 
    private dialogRef: MatDialogRef<EditUserComponent>, 
    private gs: GlobalService,
    @Inject(MAT_DIALOG_DATA) public data?: UserDto) {
    this.createUpdateUserForm()
  }

  createUpdateUserForm(): void {
    this.createUserForm = this.fb.group({
      firstName: [(this.data ? this.data.firstName : ''), Validators.required],
      lastName: [(this.data ? this.data.lastName : ''), Validators.required],
      username: [(this.data ? this.data.username : ''), Validators.required],
      designation: [(this.data ? this.data.designation : ''), Validators.required],
      cellPhone: [(this.data ? this.data.cellPhone : ''), Validators.required],
      isAdmin: [(this.data ? this.data.isAdmin : ''), Validators.required]
    });
  }

  onSubmitEditUser(): void {
    if (this.createUserForm.valid) {
      if(this.data?.id){
        this.userService.updateUser(this.data.id, this.createUserForm.value)
        .subscribe({
          next: (response) => {
            this.gs.openSnackBar("Done sucessfully!!", "Dismiss");
            this.dialogRef.close();
          },
          error: (error) => {
            this.gs.openSnackBar(`An error occured ${error.error.detail}`, "Dismiss");
          }
        });
      }else {
        this.userService.createUser(this.createUserForm.value)
        .subscribe({
          next: (response) => {
            this.gs.openSnackBar("Done sucessfully!!", "Dismiss");
            this.dialogRef.close();
          },
          error: (error) => {
            this.gs.openSnackBar(`An error occured ${error.error.detail}`, "Dismiss");
          }
        });
      }
      
    }
  }
}
