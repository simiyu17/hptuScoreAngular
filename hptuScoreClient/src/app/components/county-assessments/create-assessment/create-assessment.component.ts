import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AssessmentPillar } from '../../../models/AssessmentPillar';
import { PillarsService } from '../../../services/pillars.service';
import { PillarCategory } from '../../../models/PillarCategory';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { CountyAssessment } from '../../../models/CountyAssessment';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';
import { MatRadioChange, MatRadioModule } from '@angular/material/radio';
import { CountyDto } from '../../../dto/CountyDto';
import { MatOptionModule } from '@angular/material/core';
import { UtilService } from '../../../services/util.service';
import { MatSelectModule } from '@angular/material/select';
import { CountyAssessmentDto } from '../../../dto/CountyAssessmentDto';
import { CountyAssessmentService } from '../../../services/county.assessment.service';
import { GlobalService } from '../../../services/global.service';
import { Router } from '@angular/router';


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
    MatRadioModule,
    MatOptionModule,
    MatSelectModule
  ],
  templateUrl: './create-assessment.component.html',
  styleUrl: './create-assessment.component.scss'
})
export class CreateAssessmentComponent implements OnInit {

  pillars: AssessmentPillar[] = []

  counties: CountyDto[] = []

  displayedColumns: string[] = [
    'category',
    'choiceScore',
    'scoreRemarks'
  ];

  firstFormGroup = this._formBuilder.group({
    assessmentQuarter: ['', Validators.required],
    assessmentYear: ['', Validators.required],
    assessmentLevel: ['', Validators.required],
    countyCode: ['', Validators.required]
  });

  categories?: PillarCategory[] = [];

  pillarAssessmentMap: Map<number, CountyAssessment[]> = new Map<number, CountyAssessment[]>;
  quartersList: {value: string, display: string}[] = []; 
  assessmentYearsList: {value: string, display: string}[] = []; 

  constructor(
    private _formBuilder: FormBuilder,
    private pillarService: PillarsService,
    private utilService: UtilService,
    private countyAssessService: CountyAssessmentService,
    private gs: GlobalService,
    private router: Router
  ) { }


  ngOnInit(): void {
    this.quartersList = this.gs.assessmentQuarters()
    this.assessmentYearsList = this.gs.assessmentYears();
    this.getAvailablePillars();
    this.getAvailableCounties();
  }

  getAvailablePillars(): void {
    this.pillarService.getAllPillars()
      .subscribe({
        next: (response) => {
          this.pillars = response;
          this.createAssessmentScorePlaceHolder(this.pillars, null)
        },
        error: (error) => { }
      });
  }

  getAvailableCounties(): void {
    this.utilService.getAllKenyanCounties()
      .subscribe({
        next: (response) => {
          this.counties = response;
        },
        error: (error) => { }
      });
  }


  createAssessmentScorePlaceHolder(pillars: AssessmentPillar[], quarter: string | null): void {
    pillars.forEach(pillar => {
      this.pillarService.getAllCategoriesByPillarId(pillar.id, quarter)
        .subscribe({
          next: (response) => {
            this.categories = response;
            this.categories?.map(c => c.pillarId = pillar.id);
            this.pillarAssessmentMap.set(pillar.id, this.categoriesToAssessment(this.categories, pillar.pillarName));
            pillar.dataSource = this.categoriesToAssessment(this.categories, pillar.pillarName);
            pillar.formArray = new FormArray(
              pillar.dataSource.map(
                (x: any) =>
                  new FormGroup({
                    choiceScore: new FormControl(x.choiceScore),
                    scoreRemarks: new FormControl(x.scoreRemarks),
                    pillarId: new FormControl(x.pillarId),
                    pillarName: new FormControl(x.pillarName),
                    category: new FormControl(x.category),
                    choiceOne: new FormControl(x.choiceOne),
                    choiceOneScore: new FormControl(x.choiceOneScore),
                    choiceTwo: new FormControl(x.choiceTwo),
                    choiceTwoScore: new FormControl(x.choiceTwoScore),
                    choiceThree: new FormControl(x.choiceThree),
                    choiceThreeScore: new FormControl(x.choiceThreeScore),
                    choiceFour: new FormControl(x.choiceFour),
                    choiceFourScore: new FormControl(x.choiceFourScore),
                    categoryOrder: new FormControl(x.categoryOrder),
                    choiceText: new FormControl(x.choiceText),
                    maxScore: new FormControl(x.maxScore)
                  })
              )
            );
          },
          error: (error) => { }
        })
    })
  }

  categoriesToAssessment(categorys: PillarCategory[] | undefined, pillarName: string): CountyAssessment[] {
    let result: CountyAssessment[] = [];
    categorys?.forEach(cat => {
      let assess: CountyAssessment = {
        id: 0,
        pillarId: cat.pillarId,
        pillarName: pillarName,
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

  changeCapabilityChoice(e: MatRadioChange, pillar: AssessmentPillar, index: number, controlName: string): void {
    this.getControl(pillar, index, controlName).setValue(e.source._inputElement.nativeElement.labels?.item(0).innerHTML)
  }

  submitScores(): void {
    let countyAssessmentDto: CountyAssessmentDto = {
      assessmentMetaDataDto: {
        assessmentQuarter: this.firstFormGroup.value.assessmentQuarter ? this.firstFormGroup.value.assessmentQuarter : null,
        assessmentYear: this.firstFormGroup.value.assessmentYear ? this.firstFormGroup.value.assessmentYear : null,
        assessmentLevel: this.firstFormGroup.value.assessmentLevel ? this.firstFormGroup.value.assessmentLevel : null,
        countyCode: this.firstFormGroup.value.countyCode ? this.firstFormGroup.value.countyCode : null
      },
      assessments: this.pillars.flatMap(p => p.formArray.value)
    }
    this.countyAssessService.createCountyAssessment(countyAssessmentDto)
      .subscribe({
        next: (response) => {
          this.gs.openSnackBar(response.message, "Dismiss");
          if(response.success){
            this.router.navigateByUrl('county-assessments');
          }
          
        },
        error: (error) => {
          this.gs.openSnackBar(`An error occured ${error.error.detail}`, "Dismiss");
        }
      });
  }

  selectedQuarterChanged(){
    const selectedQuarter = this.firstFormGroup.controls['assessmentQuarter'].value;
    this.createAssessmentScorePlaceHolder(this.pillars, selectedQuarter)
  }

}
