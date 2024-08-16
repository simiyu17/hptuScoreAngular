import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule, MatOptionModule } from '@angular/material/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { CountyDto } from '../../../dto/CountyDto';
import { AuthService } from '../../../services/auth.service';
import { GlobalService } from '../../../services/global.service';
import { UtilService } from '../../../services/util.service';
import { MatDatepickerModule } from '@angular/material/datepicker';
import moment from 'moment';

@Component({
  selector: 'app-hptu-dashboard-filter',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatOptionModule,
    ReactiveFormsModule, 
    FormsModule, 
    MatDialogModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './hptu-dashboard-filter.component.html',
  styleUrl: './hptu-dashboard-filter.component.scss'
})
export class HptuDashboardFilterComponent {

  counties: CountyDto[] = []
  filterFormGroup: FormGroup = this._formBuilder.group({}); 

  constructor(
    private _formBuilder: FormBuilder,
    private utilService: UtilService,
    private gs: GlobalService,
    private authService: AuthService,
    private dialogRef: MatDialogRef<HptuDashboardFilterComponent>, 
  ) { 
    this.createFilterForm();
  }

  createFilterForm(): void {
    this.filterFormGroup = this._formBuilder.group({
      assessmentDate: ['', Validators.required],
      countyCode: ['', Validators.required]
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

  ngOnInit(): void {
    this.getAvailableCounties();
  }

  changeDatePicker(): any {
    this.filterFormGroup.value.assessmentDate = moment(this.filterFormGroup.value.assessmentDate).format('YYYY-MM-DD');
  }

  onSubmitEditPillar(): void {
    if (this.filterFormGroup.valid) {
      this.utilService.onAssessmentDataReceived(this.filterFormGroup.value)
      this.authService.storeUserCurrentDashBoardFilters(this.filterFormGroup.value)
      this.dialogRef.close();
    }
  }
}
