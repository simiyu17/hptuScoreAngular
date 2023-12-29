import { Component, Inject } from '@angular/core';
import { SharedModule } from '../../../modules/shared/shared.module';
import { MaterialModule } from '../../../modules/material/material.module';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PillarsService } from '../../../services/pillars.service';
import { Router } from '@angular/router';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { GlobalService } from '../../../services/global.service';
import { EditCategoryDto } from '../../../dto/EditCategoryDto';

@Component({
  selector: 'app-edit-category',
  standalone: true,
  imports: [SharedModule, MaterialModule],
  templateUrl: './edit-category.component.html',
  styleUrl: './edit-category.component.scss'
})
export class EditCategoryComponent {

  createPillarCategoryForm: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, 
    private pillarService: PillarsService, 
    private router: Router, 
    private dialogRef: MatDialogRef<EditCategoryComponent>, 
    private gs: GlobalService,
    @Inject(MAT_DIALOG_DATA) public data: EditCategoryDto) {
    this.createNewPillarForm()
  }

  createNewPillarForm(): void {
    if('newCategory' in this.data.category){
      this.createPillarCategoryForm = this.fb.group({
        category: ['', Validators.required],
        categoryOrder: ['', Validators.required],
        choiceOne: ['', Validators.required],
        choiceOneScore: ['', Validators.required],
        choiceTwo: ['', Validators.required],
        choiceTwoScore: ['', Validators.required],
        choiceThree: ['', Validators.required],
        choiceThreeScore: ['', Validators.required],
        choiceFour: ['', Validators.required],
        choiceFourScore: ['', Validators.required]
      });
    }else {
      const currentCategory = this.data.category;
      this.createPillarCategoryForm = this.fb.group({
        category: [currentCategory.category, Validators.required],
        categoryOrder: [currentCategory.categoryOrder, Validators.required],
        choiceOne: [currentCategory.choiceOne, Validators.required],
        choiceOneScore: [currentCategory.choiceOneScore, Validators.required],
        choiceTwo: [currentCategory.choiceTwo, Validators.required],
        choiceTwoScore: [currentCategory.choiceTwoScore, Validators.required],
        choiceThree: [currentCategory.choiceThree, Validators.required],
        choiceThreeScore: [currentCategory.choiceThreeScore, Validators.required],
        choiceFour: [currentCategory.choiceFour, Validators.required],
        choiceFourScore: [currentCategory.choiceFourScore, Validators.required]
      });
    }
    
  }

  onSubmitEditPillarCategory(): void {
    if (this.createPillarCategoryForm.valid) {
      if('newCategory' in this.data.category){
        this.pillarService.createPillarCategory(this.data.pillarId, this.createPillarCategoryForm.value)
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
      }else {
        this.pillarService.updatePillarCategoryById(this.data.pillarId, this.data.category.id, this.createPillarCategoryForm.value)
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
}
