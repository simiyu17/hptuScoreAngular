import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { ChartModule } from 'primeng/chart';
import { CountySummaryDto } from '../../../dto/CountySummaryDto';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pillar-summary',
  standalone: true,
  imports: [
    ChartModule, 
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './pillar-summary.component.html',
  styleUrl: './pillar-summary.component.scss'
})
export class PillarSummaryComponent implements OnInit {
  data: any;
  options: any;
  @Input() pillarSummaries: CountySummaryDto[] = []
  responsiveOptions: any[] | undefined;

  constructor(private router: Router){}

  ngOnChanges(changes: SimpleChanges) {
    this.drawSummaryGraph()
  }

  openPillarDetails(summary: CountySummaryDto): void {
    this.router.navigateByUrl(`/dashboard/${summary.metaDataId}/${summary.pillarName}`)
  }

  ngOnInit(): void {
    this.drawSummaryGraph();

    this.responsiveOptions = [
      {
          breakpoint: '1199px',
          numVisible: 1,
          numScroll: 1
      },
      {
          breakpoint: '991px',
          numVisible: 2,
          numScroll: 1
      },
      {
          breakpoint: '767px',
          numVisible: 1,
          numScroll: 1
      }
  ];
  }

  drawSummaryGraph(): void {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.data = {
      labels: ['Cat 1', 'Cat 2', 'Cat 3', 'Cat 4', 'Cat 5', 'Cat 6', 'Cat 7'],
      datasets: [
        {
          label: '',
          backgroundColor: documentStyle.getPropertyValue('--blue-500'),
          borderColor: documentStyle.getPropertyValue('--blue-500'),
          data: [1, 3, 2, 4, 3, 2, 1]
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
