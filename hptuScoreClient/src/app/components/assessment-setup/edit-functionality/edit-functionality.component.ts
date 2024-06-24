import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FunctionalityDto } from '../../../dto/FunctionalityDto';
import { FunctionalityService } from '../../../services/functionality.service';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { GlobalService } from '../../../services/global.service';

@Component({
  selector: 'app-edit-functionality',
  standalone: true,
  imports: [
    ReactiveFormsModule, 
    FormsModule, 
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatDialogModule,
    CommonModule,
    FlexLayoutModule
  ],
  templateUrl: './edit-functionality.component.html',
  styleUrl: './edit-functionality.component.scss'
})
export class EditFunctionalityComponent implements OnInit {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private functionalityService: FunctionalityService,
    public dialogRef: MatDialogRef<EditFunctionalityComponent>,
    private gs: GlobalService,
    @Inject(MAT_DIALOG_DATA) public data: { isEditMode: boolean; functionality?: FunctionalityDto }
  ) {
    this.form = this.fb.group({
      id: [null],
      hptuName: ['', Validators.required],
      functionalityOrder: [null, Validators.required],
      description: ['', Validators.required]
    });

    if (this.data.isEditMode && this.data.functionality) {
      this.form.patchValue(this.data.functionality);
    }
  }

  ngOnInit(): void {}

  save(): void {
    if (this.form.valid) {
      const functionality: FunctionalityDto = this.form.value;
      if (this.data.isEditMode) {
        this.functionalityService.updateFunctionality(functionality).subscribe({
          next: (response) => {
            this.gs.openSnackBar("Done sucessfully!!", "Dismiss");
            this.dialogRef.close('saved');
          },
          error: (error) => {
            this.gs.openSnackBar(`An error occured: ${error.error.message}`, "Dismiss");
          }
        });
      } else {
        this.functionalityService.addFunctionality(functionality).subscribe(
        {
          next: (response) => {
            this.gs.openSnackBar("Done sucessfully!!", "Dismiss");
            this.dialogRef.close('saved');
          },
          error: (error) => {
            this.gs.openSnackBar(`An error occured: ${error.error.message}`, "Dismiss");
          }
        });
      }
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }

}
