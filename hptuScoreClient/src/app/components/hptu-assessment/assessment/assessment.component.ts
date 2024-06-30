import { Component, OnInit } from '@angular/core';
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
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatOptionModule } from '@angular/material/core';
import { UtilService } from '../../../services/util.service';
import { MatSelectModule } from '@angular/material/select';
import { CountyAssessmentDto } from '../../../dto/CountyAssessmentDto';
import { CountyAssessmentService } from '../../../services/county.assessment.service';
import { GlobalService } from '../../../services/global.service';
import { Router } from '@angular/router';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { CustomDateAdapter, CUSTOM_DATE_FORMATS } from '../../../util/CustomDateAdapter';


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
    MatNativeDateModule,
    MatDatepickerModule
  ],
  providers: [
    { provide: DateAdapter, useClass: CustomDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: CUSTOM_DATE_FORMATS },
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' }
  ],
  templateUrl: './assessment.component.html',
  styleUrl: './assessment.component.scss'
})
export class AssessmentComponent implements OnInit {

  counties: CountyDto[] = []

  displayedColumns: string[] = [
    'category',
    'choiceScore',
    'scoreRemarks'
  ];

  firstFormGroup = this._formBuilder.group({
    countyCode: ['', Validators.required],
    assessmentDate: ['', Validators.required]
  });

  pillars: any[] = []
  constructor(
    private _formBuilder: FormBuilder,
    private pillarService: PillarsService,
    private utilService: UtilService,
    private countyAssessService: CountyAssessmentService,
    private gs: GlobalService,
    private router: Router
  ) { }

  getAvailablePillars(): void {
    this.pillarService.getAllPillars()
      .subscribe({
        next: (response) => {
          //this.pillars = response;
          //this.createAssessmentScorePlaceHolder(this.pillars, null)
        },
        error: (error) => { }
      });
  }


  ngOnInit(): void {
    this.getAvailableCounties();
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

  getControl(pillar: AssessmentPillar, index: number, controlName: string): FormControl {
    return (pillar.formArray.at(index) as FormGroup).get(controlName) as FormControl;
  }

  changeCapabilityChoice(e: MatRadioChange, pillar: AssessmentPillar, index: number, controlName: string): void {
    this.getControl(pillar, index, controlName).setValue(e.source._inputElement.nativeElement.labels?.item(0).innerHTML)
  }


  submitScores(): void {
   
  }
}
