import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { PillarsService } from '../../../../services/pillars.service';
import { GlobalService } from '../../../../services/global.service';
import { EditCategoryDto } from '../../../../dto/EditCategoryDto';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-edit-category',
  standalone: true,
  imports: [
    ReactiveFormsModule, 
    FormsModule, 
    MatInputModule,
    MatFormFieldModule,
    MatDialogModule,
    MatButtonModule,
    MatOptionModule,
    MatSelectModule
  ],
  templateUrl: './edit-category.component.html',
  styleUrl: './edit-category.component.scss'
})
export class EditCategoryComponent implements OnInit{

  formTitle: string = 'Edit Category';
  formBtnText: string = 'Update';
  createPillarCategoryForm: FormGroup = this.fb.group({});

  quartersList: {value: string, display: string}[] = []; 
  selected: string[] = []

  constructor(private fb: FormBuilder, 
    private pillarService: PillarsService, 
    private dialogRef: MatDialogRef<EditCategoryComponent>, 
    private gs: GlobalService,
    @Inject(MAT_DIALOG_DATA) public data: EditCategoryDto) {
    this.createNewPillarForm()
  }
  ngOnInit(): void {
    this.quartersList = this.gs.assessmentQuarters()
  }

  createNewPillarForm(): void {
    if('newCategory' in this.data.category){
      this.formTitle = 'New Category';
      this.formBtnText = 'Submit';
      this.createPillarCategoryForm = this.fb.group({
        category: ['', Validators.required],
        categoryOrder: ['', Validators.min(1)],
        allowedQuarters: ['', Validators.length > 0],
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
      this.data.allowedQuarters = currentCategory.allowedQuarters;
      this.createPillarCategoryForm = this.fb.group({
        category: [currentCategory.category, Validators.required],
        categoryOrder: [currentCategory.categoryOrder, Validators.min(1)],
        allowedQuarters: [currentCategory.allowedQuarters, Validators.length > 0],
        choiceOne: [currentCategory.choiceOne, Validators.required],
        choiceOneScore: [currentCategory.choiceOneScore, Validators.min(1)],
        choiceTwo: [currentCategory.choiceTwo, Validators.required],
        choiceTwoScore: [currentCategory.choiceTwoScore, Validators.min(1)],
        choiceThree: [currentCategory.choiceThree, Validators.required],
        choiceThreeScore: [currentCategory.choiceThreeScore, Validators.min(1)],
        choiceFour: [currentCategory.choiceFour, Validators.required],
        choiceFourScore: [currentCategory.choiceFourScore, Validators.min(1)]
      });
      this.selected = this.data.allowedQuarters
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
            this.gs.openSnackBar(`An error occured ${error.error.detail}`, "Dismiss");
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
            this.gs.openSnackBar(`An error occured ${error.error.detail}`, "Dismiss");
          }
        });
      }
    }
  }
}
