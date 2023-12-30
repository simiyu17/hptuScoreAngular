import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AssessmentPillar } from '../../models/AssessmentPillar';
import { PillarsService } from '../../services/pillars.service';
import { PillarCategory } from '../../models/PillarCategory';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { CountyAssessment } from '../../models/CountyAssessment';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import {MatRadioModule} from '@angular/material/radio';


@Component({
  selector: 'app-create-assessment',
  standalone: true,
  imports: [
    MatStepperModule,
    ReactiveFormsModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatToolbarModule,
    MatTableModule,
    MatRadioModule
  ],
  templateUrl: './create-assessment.component.html',
  styleUrl: './create-assessment.component.scss'
})
export class CreateAssessmentComponent {

  pillars: AssessmentPillar[] = []

  displayedColumns: string[] = [
    'category', 
    'choiceScore', 
    'scoreRemarks'
  ];

  displayedColumns2: string[] = [
    'pillarId', 
    'choiceOne', 
    'choiceOneScore', 
    'choiceTwo', 
    'choiceTwoScore', 
    'choiceThree', 
    'choiceThreeScore', 
    'choiceFour', 
    'choiceFourScore', 
    'categoryOrder', 
    'choiceText', 
    'maxScore', 
    'category', 
    'choiceScore', 
    'scoreRemarks'
  ];

  firstFormGroup = this._formBuilder.group({
    assessmentQuarter: ['', Validators.required],
    assessmentYear: ['', Validators.required],
    assessmentLevel: ['', Validators.required],
    countyCode: ['', Validators.required],
    countyName: ['', Validators.required]
  });

  categories?: PillarCategory[] = [];

  pillarAssessmentMap: Map<number, CountyAssessment[]> = new Map<number, CountyAssessment[]>;

  constructor(private _formBuilder: FormBuilder, private pillarService: PillarsService) { }

  ngOnInit() {
    this.pillarService.getAllPillars()
      .subscribe({
        next: (response) => {
          console.log(response);
          this.pillars = response;
          this.createAssessmentScorePlaceHolder(this.pillars)
        },
        error: (error) => { }
      });
  }


  createAssessmentScorePlaceHolder(pillars: AssessmentPillar[]): void {
    pillars.forEach(pillar => {
      this.pillarService.getAllCategoriesByPillarId(pillar.id)
        .subscribe({
          next: (response) => {
            this.categories = response;
            this.categories?.map(c => c.pillarId = pillar.id);
            this.pillarAssessmentMap.set(pillar.id, this.categoriesToAssessment(this.categories));
            pillar.dataSource = this.categoriesToAssessment(this.categories);
            pillar.formArray = new FormArray(
              pillar.dataSource.map(
                (x:any) =>
                  new FormGroup({
                    choiceScore: new FormControl(x.choiceScore),
                    scoreRemarks: new FormControl(x.scoreRemarks),
                  })
              )
            );
            console.log(this.pillarAssessmentMap)
          },
          error: (error) => { }
        })
    })
  }

  categoriesToAssessment(categorys: PillarCategory[]| undefined): CountyAssessment[] {
    let result: CountyAssessment[] = [];
    categorys?.forEach(cat => {
      let assess: CountyAssessment = {
        id: 0,
        pillarId: cat.pillarId,
        category: cat.category,
        choiceOne: cat.choiceOne,
        choiceOneScore: cat.choiceOneScore,
        choiceTwo: cat.choiceTwo,
        choiceTwoScore: cat.choiceTwoScore,
        choiceThree: cat.choiceThree,
        choiceThreeScore: cat.choiceThreeScore,
        choiceFour: cat.choiceFour,
        choiceFourScore: cat.choiceFourScore,
        categoryOrder: cat.categoryOrder,
        choiceText: '',
        choiceScore: 0,
        maxScore: cat.choiceFourScore,
        scoreRemarks: '',
      }
      result.push(assess);
    })

    return result;
  }

  getControl(pillar: AssessmentPillar, index: number, controlName: string): FormControl {
    return (pillar.formArray.at(index) as FormGroup).get(controlName) as FormControl;
  }

  submitScores(): void {
    console.log(this.firstFormGroup.value)
    console.log(this.pillars[2].formArray)
  }
}
