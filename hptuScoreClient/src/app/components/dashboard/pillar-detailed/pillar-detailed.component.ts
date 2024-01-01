import { Component, OnChanges, OnInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { CountySummaryDto } from '../../../dto/CountySummaryDto';
import { DashboardService } from '../../../services/dashboard.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ChartModule } from 'primeng/chart';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-pillar-detailed',
  standalone: true,
  imports: [
    MatTableModule,
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    ChartModule
  ],
  templateUrl: './pillar-detailed.component.html',
  styleUrl: './pillar-detailed.component.scss'
})
export class PillarDetailedComponent implements OnInit{

  displayedColumns: string[] = ['category', 'choiceScore', 'maxScore'];
  dataSource!: MatTableDataSource<CountySummaryDto>;
  countyAssessmentSummaries?: CountySummaryDto[] | any[];
  pillarSummaries: any[] = [];
  metaDataId?: any;
  pillarName?: any;
  graphLabels: string[] = []
  graphYData: number[] = []
  data: any;
  options: any;
  countuAssessmentSummaryBarChartDataPoints: { x: string, y: number }[] = [];
  constructor(
    private dashBoardService: DashboardService,
    private router: ActivatedRoute 
    ) { }


  getCountyAssessmentSummary() {
    this.dashBoardService.getCountyAssessmentSummaryByCategory(this.metaDataId, this.pillarName)
      .subscribe({
        next: (response) => {
          this.countyAssessmentSummaries = response.summary;
          this.pillarSummaries = response.summary
          this.dataSource = new MatTableDataSource(this.countyAssessmentSummaries);
          this.countuAssessmentSummaryBarChartDataPoints = response.summaryDataPoints;
          this.drawSummaryGraph(this.countuAssessmentSummaryBarChartDataPoints);
        },
        error: (error) => { }
      });
  }


  ngOnInit(): void {
    this.metaDataId = this.router.snapshot.paramMap.get('meta-id');
    this.pillarName = this.router.snapshot.paramMap.get('pillar-name');
    this.getCountyAssessmentSummary();
  }

  downLoad(): void {}

  drawSummaryGraph(dataPoints: { x: string, y: number }[]): void {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');
    dataPoints.forEach(d =>{
      this.graphLabels.push(d.x);
      this.graphYData.push(d.y);
    });

    this.data = {
      labels: this.graphLabels,
      datasets: [
        {
          label: '',
          backgroundColor: documentStyle.getPropertyValue('--blue-500'),
          borderColor: documentStyle.getPropertyValue('--blue-500'),
          data: this.graphYData
        }
      ]
    };

    this.options = {
      indexAxis: 'y',
      maintainAspectRatio: false,
      aspectRatio: 0.8,
      plugins: {
        legend: {
          labels: {
            color: textColor
          }
        }
      },
      scales: {
        x: {
          ticks: {
            color: textColorSecondary,
            font: {
              weight: 500
            }
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false
          }
        },
        y: {
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false
          }
        }
      }
    };
  }

}
