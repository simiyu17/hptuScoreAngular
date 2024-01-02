import { Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { DashboardFilterComponent } from './dashboard-filter/dashboard-filter.component';
import { CountySummaryDto } from '../../dto/CountySummaryDto';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDialog } from '@angular/material/dialog';
import { DashboardService } from '../../services/dashboard.service';
import { AssessmentGraphComponent } from './assessment-graph/assessment-graph.component';
import { MatButtonModule } from '@angular/material/button';
import { PillarSummaryComponent } from './pillar-summary/pillar-summary.component';
import {SafePipe } from '../../util/safe.pipe'

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    MatTableModule,
    MatToolbarModule,
    AssessmentGraphComponent,
    PillarSummaryComponent,
    MatButtonModule,
    SafePipe
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  displayedColumns: string[] = ['pillarName', 'maxScore', 'choiceScore', 'scorePercent', 'remark'];
  dataSource!: MatTableDataSource<CountySummaryDto>;
  countuAssessmentSummaries: CountySummaryDto[] | any[] = [];
  pillarSummaries: any[] = [];
  countuAssessmentSummaryBarChartDataPoints?: { x: string, y: number }[];
  excelBase64String?: string;
  constructor(
    public dialog: MatDialog,
    private dashBoardService: DashboardService,
  ) { }

  openNewPillarDialog() {
    const dialogRef = this.dialog.open(DashboardFilterComponent);

    dialogRef.afterClosed().subscribe({
      next: (res) => {
        this.getCountyAssessmentSummary();
      }
    });
  }

  getCountyAssessmentSummary() {
    this.dashBoardService.getCountyAssessmentSummaryByPillar()
      .subscribe({
        next: (response) => {
          this.countuAssessmentSummaries = response.summary;
          this.pillarSummaries = response.summary
          this.dataSource = new MatTableDataSource(this.countuAssessmentSummaries);
          this.countuAssessmentSummaryBarChartDataPoints = [...response.summaryDataPoints];
          if (this.countuAssessmentSummaries?.length > 0) {
            this.exportToExcel(this.countuAssessmentSummaries.at(0).metaDataId);
          }
        },
        error: (error) => { }
      });
  }

  ngOnInit(): void {
    this.getCountyAssessmentSummary();
  }

  exportToExcel(metaDataId: number): void {
    this.dashBoardService.exportCountyAssessmentSummaryToExcel(metaDataId)
      .subscribe({
        next: (response) => {
          this.excelBase64String = response.message
        },
        error: (error) => { 
          console.log(error)
        }
      });

  }
}
