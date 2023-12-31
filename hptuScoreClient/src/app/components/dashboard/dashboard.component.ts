import { Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { DashboardFilterComponent } from './dashboard-filter/dashboard-filter.component';
import { CountySummaryDto } from '../../dto/CountySummaryDto';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDialog } from '@angular/material/dialog';
import { DashboardService } from '../../services/dashboard.service';
import { AssessmentGraphComponent } from './assessment-graph/assessment-graph.component';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    MatTableModule,
    MatToolbarModule,
    AssessmentGraphComponent,
    MatButtonModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit{

  displayedColumns: string[] = ['pillarName', 'maxScore', 'choiceScore', 'scorePercent', 'remark'];
  dataSource!: MatTableDataSource<CountySummaryDto>;
  countuAssessmentSummaries?: CountySummaryDto[];
  countuAssessmentSummaryBarChartDataPoints?: { x: string, y: number }[];
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
    this.dashBoardService.getCountyAssessmentSummary()
      .subscribe({
        next: (response) => {
          this.countuAssessmentSummaries = response;
          this.dataSource = new MatTableDataSource(this.countuAssessmentSummaries);
        },
        error: (error) => { }
      });
  }

  getCountyAssessmentSummaryBarDataPoints() {
    this.dashBoardService.getCountyAssessmentSummaryBarDataPoints()
      .subscribe({
        next: (response) => {
          this.countuAssessmentSummaryBarChartDataPoints = [...response];
        },
        error: (error) => { }
      });
  }

  ngOnInit(): void {
    this.getCountyAssessmentSummary();
    this.getCountyAssessmentSummaryBarDataPoints();
  }
}
