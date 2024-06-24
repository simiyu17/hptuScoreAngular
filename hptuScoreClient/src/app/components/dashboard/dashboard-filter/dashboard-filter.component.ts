import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CountyDto } from '../../../dto/CountyDto';
import { UtilService } from '../../../services/util.service';
import { GlobalService } from '../../../services/global.service';
import { Router } from '@angular/router';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatSelectModule } from '@angular/material/select';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-dashboard-filter',
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
    MatSelectModule
  ],
  templateUrl: './dashboard-filter.component.html',
  styleUrl: './dashboard-filter.component.scss'
})
export class DashboardFilterComponent implements OnInit {
 

  counties: CountyDto[] = []
  quartersList: {value: string, display: string}[] = []; 
  assessmentYearsList: {value: string, display: string}[] = []; 

  filterFormGroup: FormGroup = this._formBuilder.group({}); 

  constructor(
    private _formBuilder: FormBuilder,
    private utilService: UtilService,
    private gs: GlobalService,
    private authService: AuthService,
    private dialogRef: MatDialogRef<DashboardFilterComponent>, 
  ) { 
    this.createFIlterForm();
  }

  createFIlterForm(): void {
    this.filterFormGroup = this._formBuilder.group({
      assessmentQuarter: [''],
      assessmentYear: ['', Validators.required],
      countyCode: ['']
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
    this.quartersList = this.gs.assessmentQuarters()
    this.assessmentYearsList = this.gs.assessmentYears();
    this.getAvailableCounties();
  }

  onSubmitEditPillar(): void {
    if (this.filterFormGroup.valid) {
      this.utilService.onAssessmentDataReceived(this.filterFormGroup.value)
      this.authService.storeUserCurrentDashBoardFilters(this.filterFormGroup.value)
      this.dialogRef.close();
    }
  }
}
