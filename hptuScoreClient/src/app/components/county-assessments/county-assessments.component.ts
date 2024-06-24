import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CountyAssessmentMetaData } from '../../models/CountyAssessmentMetaData';
import { MatDialog } from '@angular/material/dialog';
import { GlobalService } from '../../services/global.service';
import { CountyAssessmentService } from '../../services/county.assessment.service';
import { Router, RouterLink } from '@angular/router';
import { PillarsService } from '../../services/pillars.service';
import { AssessmentPillar } from '../../models/AssessmentPillar';
import { DashboardFilterComponent } from '../dashboard/dashboard-filter/dashboard-filter.component';
import { UtilService } from '../../services/util.service';
import { ConfirmDialogModel } from '../../models/ConfirmDialogModel';
import { ConfirmDialogComponent } from '../shared/confirm-dialog/confirm-dialog.component';
import {MatTabsModule} from '@angular/material/tabs';

@Component({
  selector: 'app-county-assessments',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatFormFieldModule,
    MatTableModule,
    MatTooltipModule,
    MatIconModule,
    MatPaginatorModule,
    MatButtonModule,
    MatSortModule,
    MatInputModule,
    RouterLink,
    MatTabsModule
  ],
  templateUrl: './county-assessments.component.html',
  styleUrl: './county-assessments.component.scss'
})
export class CountyAssessmentsComponent implements OnInit, OnDestroy {

  displayedColumns: string[] = ['countyName', 'assessmentYear', 'assessmentQuarter', 'assessmentLevel', 'action'];
  dataSource!: MatTableDataSource<CountyAssessmentMetaData>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  assessments: CountyAssessmentMetaData[] = []

  pillars?: AssessmentPillar[];
  canPerformAssessment: boolean = false;
  constructor(
    public dialog: MatDialog, 
    private assessmentService: CountyAssessmentService, 
    private router: Router, 
    private gs: GlobalService,
    private pillarService: PillarsService,
    private utilService: UtilService
    ) { }


  getTopFiveAssessments2() {
    this.assessmentService.getTopFiveCountyAssessments()
      .subscribe({
        next: (response) => {
          this.assessments = response;
          this.dataSource = new MatTableDataSource(this.assessments);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (error) => { }
      });
  }

  getAvailablePillars() {
    this.pillarService.getAllPillars()
      .subscribe({
        next: (response) => {
          this.pillars = response;
          this.canPerformAssessment = this.pillars !== undefined && this.pillars.length > 0 && this.pillars.every(p => p.pillarCategoryCount > 0);
        },
        error: (error) => { }
      });
  }

  getTopFiveAssessments = () => {
    this.utilService.currentAssessmentData().subscribe((ass?: CountyAssessmentMetaData) => {
      if(ass){
        this.assessmentService.getCountyAssessmentByCountyYearAndQuater(ass)
        .subscribe({
          next: (response) => {
            this.assessments.push(response);
            this.dataSource = new MatTableDataSource(this.assessments);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          },
          error: (error) => { }
        });
      }else{
        this.getTopFiveAssessments2();
      }
    })
  }

  ngOnInit(): void {
    this.assessments = []
    this.getAvailablePillars();
    this.getTopFiveAssessments();
  }

  deleteAssessment(assessmentId: number): void {
    const dialogData = new ConfirmDialogModel("Confirm", `Are you sure you want to delete this category?`);
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      maxWidth: "400px",
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if(dialogResult){
        this.assessmentService.deleteCountyAssessmentById(assessmentId)
        .subscribe({
          next: (response) => {
            this.gs.openSnackBar(response.message, "Dismiss");
            this.getTopFiveAssessments()
          },
          error: (error) => { 
          }
        });
      }
    });
  }

  openAssessmentFilterDialog() {
    const dialogRef = this.dialog.open(DashboardFilterComponent);

    dialogRef.afterClosed().subscribe({
      next: (res) => {
        this.ngOnInit();
      }
    });
  }

  ngOnDestroy(): void {
    this.utilService.onAssessmentDataReceived()
  }
}
