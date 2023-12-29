import { Component } from '@angular/core';
import { SharedModule } from '../../modules/shared/shared.module';
import { MaterialModule } from '../../modules/material/material.module';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AssessmentPillar } from '../../models/AssessmentPillar';
import { PillarsService } from '../../services/pillars.service';
import { PillarCategory } from '../../models/PillarCategory';

@Component({
  selector: 'app-create-assessment',
  standalone: true,
  imports: [SharedModule, MaterialModule],
  templateUrl: './create-assessment.component.html',
  styleUrl: './create-assessment.component.scss'
})
export class CreateAssessmentComponent {

  pillars: AssessmentPillar[] = [
    { 'id': 1000, 'pillarName': 'Finance', 'pillarOrder': 1, 'pillarCategoryCount': 3 },
    { 'id': 1001, 'pillarName': 'HPT', 'pillarOrder': 2, 'pillarCategoryCount': 5 }
  ]

  firstFormGroup = this._formBuilder.group({
    assessmentQuarter: ['', Validators.required],
    assessmentYear: ['', Validators.required],
    assessmentLevel: ['', Validators.required],
    countyCode: ['', Validators.required],
    countyName: ['', Validators.required]
  });
  secondFormGroup = this._formBuilder.group({
    pillarName: ['', Validators.required],
    secondCtrl: ['', Validators.required],
  });

  countyAssessmentForm: FormGroup = this._formBuilder.group({});

  categories?: PillarCategory[] = [];

  constructor(private _formBuilder: FormBuilder, private pillarService: PillarsService) { }

  ngOnInit() {
    this.pillarService.getAllPillars()
      .subscribe({
        next: (response) => {
          console.log(response);
          this.pillars = response;
          this.addPillarFormGroups(this.pillars, this.countyAssessmentForm)
        },
        error: (error) => { }
      });
  }

  addPillarFormGroups(pillars: AssessmentPillar[], formGroup: FormGroup): void {
    pillars.forEach(pillar => {
      this.pillarService.getAllCategoriesByPillarId(pillar.id)
        .subscribe({
          next: (response) => {
            this.categories = response;
            this.categories?.forEach(cat => {
              const categoryForm = this._formBuilder.group({
                id: [cat.id, Validators.required],
                category: [cat.category, Validators.required],
                choiceOne: [cat.choiceOne, Validators.required],
                choiceOneScore: [cat.choiceOneScore, Validators.required],
                choiceTwo: [cat.choiceTwo, Validators.required],
                choiceTwoScore: [cat.choiceTwoScore, Validators.required],
                choiceThree: [cat.choiceThree, Validators.required],
                choiceThreeScore: [cat.choiceThreeScore, Validators.required],
                choiceFour: [cat.choiceFour, Validators.required],
                choiceFourScore: [cat.choiceFourScore, Validators.required],
                categoryOrder: [cat.categoryOrder, Validators.required],
                pillarId: [pillar.id, Validators.required]
              });
              const catArray: FormArray = this._formBuilder.array([])
              catArray.push(categoryForm);
              this.countyAssessmentForm.addControl(String(pillar.id), catArray);
            });
          },
          error: (error) => { }
        })
    })
  }

  pillarCategories(pillar: number) {
    return this.countyAssessmentForm.controls[String(pillar)] as FormArray;
  }

}
