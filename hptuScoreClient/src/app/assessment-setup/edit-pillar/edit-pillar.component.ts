import { Component, Inject } from '@angular/core';
import { MaterialModule } from '../../modules/material/material.module';
import { SharedModule } from '../../modules/shared/shared.module';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PillarsService } from '../../services/pillars.service';
import { Router } from '@angular/router';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { GlobalService } from '../../services/global.service';
import { AssessmentPillar } from '../../models/AssessmentPillar';

@Component({
  selector: 'app-edit-pillar',
  standalone: true,
  imports: [MaterialModule, SharedModule],
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
      pillarOrder: [this.data.pillarOrder, Validators.required]
    });
  }

  onSubmitEditPillar(): void {
    console.log(this.createPillarForm.valid)
    if (this.createPillarForm.valid) {
      this.pillarService.updatePillarById(this.data.id, this.createPillarForm.value)
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
