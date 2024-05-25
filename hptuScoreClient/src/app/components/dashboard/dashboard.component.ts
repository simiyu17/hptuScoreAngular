import { Component, DestroyRef, OnDestroy, OnInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { DashboardFilterComponent } from './dashboard-filter/dashboard-filter.component';
import { CountySummaryDto } from '../../dto/CountySummaryDto';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDialog } from '@angular/material/dialog';
import { DashboardService } from '../../services/dashboard.service';
import { AssessmentGraphComponent } from './assessment-graph/assessment-graph.component';
import { MatButtonModule } from '@angular/material/button';
import {SafePipe } from '../../util/safe.pipe'
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { UtilService } from '../../services/util.service';
import { CountyAssessmentMetaData } from '../../models/CountyAssessmentMetaData';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    MatTableModule,
    MatToolbarModule,
    AssessmentGraphComponent,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    SafePipe,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit, OnDestroy {

  displayedColumns: string[] = ['pillarName', 'maxScore', 'choiceScore', 'scorePercent', 'remark', 'action'];
  dataSource!: MatTableDataSource<CountySummaryDto>;
  countuAssessmentSummaries: CountySummaryDto[] | any[] = [];
  pillarSummaries: any[] = [];
  countuAssessmentSummaryBarChartDataPoints?: { x: string, y: number }[];
  excelBase64String?: string;
  
  filteredAssessment?: any;
  constructor(
    public dialog: MatDialog,
    private dashBoardService: DashboardService,
    private router: Router,
    private utilService: UtilService,
    private destroyRef: DestroyRef
  ) {   }


  openAssessmentFilterDialog() {
    const dialogRef = this.dialog.open(DashboardFilterComponent);
    dialogRef.afterClosed().subscribe({
      next: (res) => {
        this.getCountyAssessmentSummary();
      }
    });
  }

  getCountyAssessmentSummary = () => {
    this.utilService.currentAssessmentData()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((ass: CountyAssessmentMetaData) => {
      this.dashBoardService.getCountyAssessmentSummaryByPillar(ass, null)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response) => {
          this.countuAssessmentSummaries = response.summary;
          this.pillarSummaries = response.summary
          this.dataSource = new MatTableDataSource(this.countuAssessmentSummaries);
          this.countuAssessmentSummaryBarChartDataPoints = [...response.summaryDataPoints];
          if (this.countuAssessmentSummaries?.length > 0) {
            this.exportToExcel(ass);
          }
        },
        error: (error) => { }
      })
    })
  }

  ngOnInit(): void {
    this.getCountyAssessmentSummary();
  }

  exportToExcel(ass?: CountyAssessmentMetaData): void {
    this.dashBoardService.exportCountyAssessmentSummaryToExcelV2(ass)
    .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response) => {
          this.excelBase64String = response.message
        },
        error: (error) => { 
          console.log(error)
        }
      });

  }

  openPillarDetails(summary: CountySummaryDto): void {
    //this.router.navigateByUrl(`/dashboard/${summary.pillarName}`)
    

    this.utilService.currentAssessmentData()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((ass: CountyAssessmentMetaData | null) => {
      console.log(ass)
      this.router.navigate(['/dashboard/pillar-summary'], {queryParams: {
        assessmentYear: ass ? ass.assessmentYear : null, 
        assessmentQuarter: ass?.assessmentQuarter, 
        countyCode: ass?.countyCode, 
        pillarName: summary.pillarName
      }})
    })
  }

  ngOnDestroy(): void {
    this.utilService.onAssessmentDataReceived()
  }
}
