import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AssessmentPillar } from '../../../models/AssessmentPillar';
import { PillarsService } from '../../../services/pillars.service';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';
import { MatRadioChange, MatRadioModule } from '@angular/material/radio';
import { CountyDto } from '../../../dto/CountyDto';
import { MatOptionModule } from '@angular/material/core';
import { UtilService } from '../../../services/util.service';
import { MatSelectModule } from '@angular/material/select';
import { CountyAssessmentService } from '../../../services/county.assessment.service';
import { GlobalService } from '../../../services/global.service';
import { Router } from '@angular/router';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { FunctionalityDto } from '../../../dto/FunctionalityDto';
import { FunctionalityService } from '../../../services/functionality.service';
import { HptuAssessmentDto } from '../../../dto/HptuAssessmentDto';
import { QuestionSummaryDto } from '../../../dto/QuestionSummaryDto';
import { QuestionDto } from '../../../dto/QuestionDto';
import { HptuCountyAssessmentDto } from '../../../dto/HptuCountyAssessmentDto';
import moment from 'moment';

@Component({
  selector: 'app-assessment',
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
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './assessment.component.html',
  styleUrl: './assessment.component.scss'
})
export class AssessmentComponent implements OnInit {

  counties: CountyDto[] = []

  displayedColumns: string[] = [
    'questionName',
    'attainedScore'
  ];
  functionalityAssessmentMap: Map<number, HptuAssessmentDto[]> = new Map<number, HptuAssessmentDto[]>;

  firstFormGroup = this._formBuilder.group({
    countyCode: ['', Validators.required],
    assessmentDate: ['', Validators.required]
  });

  functionalities: any[] = []
  questions: any[] = []
  constructor(
    private _formBuilder: FormBuilder,
    private functionalityService: FunctionalityService,
    private utilService: UtilService,
    private countyAssessService: CountyAssessmentService,
    private gs: GlobalService,
    private router: Router
  ) { }

  getAvailableFunctionalityDtos(): void {
    this.functionalityService.getFunctionalities()
      .subscribe({
        next: (response) => {
          this.functionalities = response;
          this.functionalities.forEach(oneFunctionality => this.createAssessmentScorePlaceHolder(oneFunctionality))
        },
        error: (error) => { }
      });
  }


  ngOnInit(): void {
    this.getAvailableCounties();
    this.getAvailableFunctionalityDtos()
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

  createAssessmentScorePlaceHolder(functionality: FunctionalityDto): void {
    functionality.questions.forEach(qSummary => {
      this.questions = qSummary.questions;
      qSummary.dataSource = this.questionsToAssessment(this.questions, qSummary.id, qSummary.summary, functionality.id, functionality.hptuName);
      qSummary.formArray = new FormArray(
        qSummary.dataSource.map(
          (x: any) =>
            new FormGroup({
              functionalityName: new FormControl(x.functionalityName),
              questionSummary: new FormControl(x.questionSummary),
              questionName: new FormControl(x.questionName),
              attainedScore: new FormControl(x.attainedScore),
              maxScore: new FormControl(x.maxScore),
              summaryColor: new FormControl(x.summaryColor),
              questionSummaryId: new FormControl(x.questionSummaryId),
              functionalityId: new FormControl(x.functionalityId)
            })
        )
      );
    })
  }

  questionsToAssessment(questions: QuestionDto[] | undefined, questionSummaryId: number, 
    questionSummaryName: string, functionalityId: number, functionalityName: string): HptuAssessmentDto[] {
    let result: HptuAssessmentDto[] = [];
    questions?.forEach(ques => {
      let assess: HptuAssessmentDto = {
        functionalityName: functionalityName,
        questionSummary: questionSummaryName,
        questionName: ques.hptuQuestion,
        attainedScore: 0,
        maxScore: ques.score,
        summaryColor: '',
        questionSummaryId: questionSummaryId,
        functionalityId: functionalityId
      }
      result.push(assess);
    })

    return result;
  }

  getControl(qSummary: QuestionSummaryDto, index: number, controlName: string): FormControl {
    return (qSummary.formArray.at(index) as FormGroup).get(controlName) as FormControl;
  }

  changeCapabilityChoice(e: MatRadioChange, qSummary: QuestionSummaryDto, index: number, controlName: string): void {
    this.getControl(qSummary, index, controlName).setValue(e.source._inputElement.nativeElement.labels?.item(0).innerHTML)
  }

  changeDatePicker(): any {
    this.firstFormGroup.value.assessmentDate = moment(this.firstFormGroup.value.assessmentDate).format('YYYY-MM-DD');
  }


  submitScores(): void {
    let countyAssessmentDto: HptuCountyAssessmentDto = {
      countyCode: this.firstFormGroup.value.countyCode ? this.firstFormGroup.value.countyCode : null,
      assessmentDate: this.firstFormGroup.value.assessmentDate ? this.firstFormGroup.value.assessmentDate : null,
      assessments: this.functionalities.flatMap(f => f.questions).flatMap(summa => summa.formArray.value)
    }
    console.log(countyAssessmentDto)
    this.countyAssessService.createCountyHPTUAssessment(countyAssessmentDto)
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
}
