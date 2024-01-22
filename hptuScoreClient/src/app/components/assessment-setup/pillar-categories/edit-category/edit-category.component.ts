import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { PillarsService } from '../../../../services/pillars.service';
import { GlobalService } from '../../../../services/global.service';
import { EditCategoryDto } from '../../../../dto/EditCategoryDto';

@Component({
  selector: 'app-edit-category',
  standalone: true,
  imports: [
    ReactiveFormsModule, 
    FormsModule, 
    MatInputModule,
    MatFormFieldModule,
    MatDialogModule,
    MatButtonModule
  ],
  templateUrl: './edit-category.component.html',
  styleUrl: './edit-category.component.scss'
})
export class EditCategoryComponent {

  formTitle: string = 'Edit Category';
  formBtnText: string = 'Update';
  createPillarCategoryForm: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, 
    private pillarService: PillarsService, 
    private dialogRef: MatDialogRef<EditCategoryComponent>, 
    private gs: GlobalService,
    @Inject(MAT_DIALOG_DATA) public data: EditCategoryDto) {
    this.createNewPillarForm()
  }

  createNewPillarForm(): void {
    if('newCategory' in this.data.category){
      this.formTitle = 'New Category';
      this.formBtnText = 'Submit';
      this.createPillarCategoryForm = this.fb.group({
        category: ['', Validators.required],
        categoryOrder: ['', Validators.min(1)],
        choiceOne: ['', Validators.required],
        choiceOneScore: ['', Validators.min(1)],
        choiceTwo: ['', Validators.required],
        choiceTwoScore: ['', Validators.min(1)],
        choiceThree: ['', Validators.required],
        choiceThreeScore: ['', Validators.min(1)],
        choiceFour: ['', Validators.required],
        choiceFourScore: ['', Validators.min(1)]
      });
    }else {
      const currentCategory = this.data.category;
      this.createPillarCategoryForm = this.fb.group({
        category: [currentCategory.category, Validators.required],
        categoryOrder: [currentCategory.categoryOrder, Validators.min(1)],
        choiceOne: [currentCategory.choiceOne, Validators.required],
        choiceOneScore: [currentCategory.choiceOneScore, Validators.min(1)],
        choiceTwo: [currentCategory.choiceTwo, Validators.required],
        choiceTwoScore: [currentCategory.choiceTwoScore, Validators.min(1)],
        choiceThree: [currentCategory.choiceThree, Validators.required],
        choiceThreeScore: [currentCategory.choiceThreeScore, Validators.min(1)],
        choiceFour: [currentCategory.choiceFour, Validators.required],
        choiceFourScore: [currentCategory.choiceFourScore, Validators.min(1)]
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
