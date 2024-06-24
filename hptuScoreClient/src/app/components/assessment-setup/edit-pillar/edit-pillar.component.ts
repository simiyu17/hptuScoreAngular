import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { PillarsService } from '../../../services/pillars.service';
import { GlobalService } from '../../../services/global.service';
import { AssessmentPillar } from '../../../models/AssessmentPillar';

@Component({
  selector: 'app-edit-pillar',
  standalone: true,
  imports: [
    ReactiveFormsModule, 
    FormsModule, 
    MatInputModule,
    MatFormFieldModule,
    MatDialogModule,
    MatButtonModule
  ],
  templateUrl: './edit-pillar.component.html',
  styleUrl: './edit-pillar.component.scss'
})
export class EditPillarComponent {

  createPillarForm: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, 
    private pillarService: PillarsService, 
    private router: Router, 
    private dialogRef: MatDialogRef<EditPillarComponent>, 
    private gs: GlobalService,
    @Inject(MAT_DIALOG_DATA) public data: AssessmentPillar) {
    this.createNewPillarForm()
  }

  createNewPillarForm(): void {
    this.createPillarForm = this.fb.group({
      pillarName: [this.data.pillarName, Validators.required],
      pillarOrder: [this.data.pillarOrder, Validators.min(1)]
    });
  }

  onSubmitEditPillar(): void {
    if (this.createPillarForm.valid) {
      this.pillarService.updatePillarById(this.data.id, this.createPillarForm.value)
        .subscribe({
          next: (response) => {
            this.gs.openSnackBar("Done sucessfully!!", "Dismiss");
            this.dialogRef.close();
          },
          error: (error) => {
            this.gs.openSnackBar(`An error occured: ${error.error.detail}`, "Dismiss");
          }
        });
    }
  }

}
