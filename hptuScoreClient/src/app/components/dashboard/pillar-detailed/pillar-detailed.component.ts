import { Component, DestroyRef, OnChanges, OnInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { CountySummaryDto } from '../../../dto/CountySummaryDto';
import { DashboardService } from '../../../services/dashboard.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ChartModule } from 'primeng/chart';
import { ActivatedRoute } from '@angular/router';
import { UtilService } from '../../../services/util.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CountyAssessmentMetaData } from '../../../models/CountyAssessmentMetaData';
import { Subscription } from 'rxjs';

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
  sub?: Subscription;
  countuAssessmentSummaryBarChartDataPoints: { x: string, y: number }[] = [];
  constructor(
    private dashBoardService: DashboardService,
    private router: ActivatedRoute,
    private utilService: UtilService,
    private destroyRef: DestroyRef
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

  getCountyAssessmentSummaryV3(pillarName: string , ass: CountyAssessmentMetaData) {
    this.dashBoardService.getCountyAssessmentSummaryByPillar(ass, pillarName)
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

  getCountyAssessmentSummaryV2 = (pillarName: string) => {
    this.utilService.currentAssessmentData()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((ass: CountyAssessmentMetaData) => {
      this.dashBoardService.getCountyAssessmentSummaryByPillar(ass, pillarName)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response) => {
          this.countyAssessmentSummaries = response.summary;
          this.pillarSummaries = response.summary
          this.dataSource = new MatTableDataSource(this.countyAssessmentSummaries);
          this.countuAssessmentSummaryBarChartDataPoints = response.summaryDataPoints;
          this.drawSummaryGraph(this.countuAssessmentSummaryBarChartDataPoints);
        },
        error: (error) => { }
      })
    })
  }


  ngOnInit(): void {
   // console.log(this.router.snapshot)
    this.metaDataId = this.router.snapshot.paramMap.get('meta-id');
    this.pillarName = this.router.snapshot.paramMap.get('pillar-name');
    
    this.sub = this.router.queryParams.subscribe(params => {
      console.log(params)
      const pillar = params['pillarName'];
      const year = params['assessmentYear'];
      const quarter = params['assessmentQuarter'];
      const county = params['countyCode'];
      if(pillar){
        this.getCountyAssessmentSummaryV3(pillar, {assessmentYear: '2023', assessmentQuarter: null, countyCode: null, assessmentLevel: null});
      }
      });
    
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
