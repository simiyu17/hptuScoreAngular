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

  filterFormGroup: FormGroup = this._formBuilder.group({}); 

  constructor(
    private _formBuilder: FormBuilder,
    private utilService: UtilService,
    private gs: GlobalService,
    private router: Router,
    private dialogRef: MatDialogRef<DashboardFilterComponent>, 
  ) { 
    this.createFIlterForm();
  }

  createFIlterForm(): void {
    this.filterFormGroup = this._formBuilder.group({
      assessmentQuarter: ['', Validators.required],
      assessmentYear: ['', Validators.required],
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

  onSubmitEditPillar(): void {
    if (this.filterFormGroup.valid) {
      this.utilService.onAssessmentDataReceived(this.filterFormGroup.value)
      this.dialogRef.close();
    }
  }
}
