import { Component } from '@angular/core';
import { PillarsService } from '../../services/pillars.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { GlobalService } from '../../services/global.service';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-create-pillar',
  standalone: true,
  imports: [
    ReactiveFormsModule, 
    FormsModule, 
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatDialogModule
  ],
  templateUrl: './create-pillar.component.html',
  styleUrl: './create-pillar.component.scss'
})
export class CreatePillarComponent {

  createPillarForm: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, private pillarService: PillarsService, private router: Router, private dialogRef: MatDialogRef<CreatePillarComponent>, private gs: GlobalService) {
    this.createNewPillarForm()
  }

  createNewPillarForm(): void {
    this.createPillarForm = this.fb.group({
      pillarName: [null, Validators.required],
      pillarOrder: [null, Validators.required]
    });
  }

  onSubmitNewPillar(): void {
    console.log(this.createPillarForm.valid)
    if (this.createPillarForm.valid) {
      this.pillarService.createPillar(this.createPillarForm.value)
        .subscribe({
          next: (response) => {
            this.gs.openSnackBar("Done sucessfully!!", "Dismiss");
            this.dialogRef.close();
          },
          error: (error) => {
            console.log(error)
            if (error.error.message) {
              alert(error.error.message);
            }
            this.gs.openSnackBar(`An error occured ${error.error}`, "Dismiss");
            console.log(error)
          }
        });
    }
  }

}
