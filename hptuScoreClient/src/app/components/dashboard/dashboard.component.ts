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
import { AuthService } from '../../services/auth.service';
import {MatTabsModule} from '@angular/material/tabs';
import { HptuAssessmentGraphComponent } from "./hptu-assessment-graph/hptu-assessment-graph.component";
import { HptuCountySummaryDto } from '../../dto/HptuCountySummaryDto';
import { HptuDashboardFilterComponent } from './hptu-dashboard-filter/hptu-dashboard-filter.component';

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
    MatInputModule,
    MatTabsModule,
    HptuAssessmentGraphComponent
],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit, OnDestroy {

  displayedColumns: string[] = ['pillarName', 'maxScore', 'choiceScore', 'scorePercent', 'remark', 'action'];
  hptuDisplayedColumns: string[] = ['functionalityName', 'maxScore', 'attainedScore', 'action'];
  dataSource!: MatTableDataSource<CountySummaryDto>;
  hptuDdataSource!: MatTableDataSource<HptuCountySummaryDto>;
  countyAssessmentSummaries: CountySummaryDto[] | any[] = [];
  hptuCountyAssessmentSummaries: HptuCountySummaryDto[] | any[] = [];
  pillarSummaries: any[] = [];
  functionalitySummaries: any[] = [];
  countyAssessmentSummaryBarChartDataPoints?: { x: string, y: number }[];
  hptuCountyAssessmentSummaryBarChartDataPoints?: { x: string, y: number }[];
  excelBase64String?: string;
  
  filteredAssessment?: any;
  constructor(
    public dialog: MatDialog,
    private dashBoardService: DashboardService,
    private router: Router,
    private utilService: UtilService,
    private destroyRef: DestroyRef,
    private authService: AuthService
  ) {   }


  openAssessmentFilterDialog() {
    const dialogRef = this.dialog.open(DashboardFilterComponent);
    dialogRef.afterClosed().subscribe({
      next: (res) => {
        this.getCountyAssessmentSummary();
      }
    });
  }

  openHptuAssessmentFilterDialog() {
    const dialogRef = this.dialog.open(HptuDashboardFilterComponent);
    dialogRef.afterClosed().subscribe({
      next: (res) => {
        this.getHPTUCountyAssessmentSummary();
      }
    });
  }

  getCountyAssessmentSummary = () => {
      this.dashBoardService.getCountyAssessmentSummaryByPillar(this.authService.retrieveUserCurrentDashBoardFilters(), null)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response) => {
          this.countyAssessmentSummaries = response.summary;
          this.pillarSummaries = response.summary
          this.dataSource = new MatTableDataSource(this.countyAssessmentSummaries);
          this.countyAssessmentSummaryBarChartDataPoints = [...response.summaryDataPoints];
          if (this.countyAssessmentSummaries?.length > 0) {
            this.exportToExcel(this.authService.retrieveUserCurrentDashBoardFilters());
          }
        },
        error: (error) => { }
      })
  }

  getHPTUCountyAssessmentSummary = () => {
    this.dashBoardService.getHPTUCountyAssessmentSummary(null, null, null)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (response) => {
        this.hptuCountyAssessmentSummaries = response.summary;
        this.functionalitySummaries = response.summary
        this.hptuDdataSource = new MatTableDataSource(this.hptuCountyAssessmentSummaries);
        this.hptuCountyAssessmentSummaryBarChartDataPoints = [...response.summaryDataPoints];
      },
      error: (error) => { }
    })
}


  ngOnInit(): void {
    this.getCountyAssessmentSummary();
    this.getHPTUCountyAssessmentSummary();
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
    this.router.navigate(['/dashboard/pillar-summary'], {queryParams: {
      pillarName: summary.pillarName
    }})
    
  }

  ngOnDestroy(): void {
    this.utilService.onAssessmentDataReceived()
  }
}
