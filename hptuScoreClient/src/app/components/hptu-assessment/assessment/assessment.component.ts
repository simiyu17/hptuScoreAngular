import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
//import {provideMomentDateAdapter} from '@angular/material-moment-adapter';
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
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatOptionModule } from '@angular/material/core';
import { UtilService } from '../../../services/util.service';
import { MatSelectModule } from '@angular/material/select';
import { CountyAssessmentService } from '../../../services/county.assessment.service';
import { GlobalService } from '../../../services/global.service';
import { Router } from '@angular/router';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import * as _moment from 'moment';
// tslint:disable-next-line:no-duplicate-imports
import {default as _rollupMoment} from 'moment';

const moment = _rollupMoment || _moment;

// See the Moment.js docs for the meaning of these formats:
// https://momentjs.com/docs/#/displaying/format/
export const MY_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'LL',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};


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
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    // Moment can be provided globally to your app by adding `provideMomentDateAdapter`
    // to your app config. We provide it at the component level here, due to limitations
    // of our example generation script.
    //provideMomentDateAdapter(MY_FORMATS)
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
