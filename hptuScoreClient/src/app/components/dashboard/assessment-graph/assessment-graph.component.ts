import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import {MatCardModule} from '@angular/material/card';

@Component({
  selector: 'app-assessment-graph',
  standalone: true,
  imports: [ChartModule, MatCardModule],
  templateUrl: './assessment-graph.component.html',
  styleUrl: './assessment-graph.component.scss'
})
export class AssessmentGraphComponent implements OnInit, OnChanges {

  basicData: any;

  basicOptions: any;

  @Input() countuAssessmentSummaryBarChartDataPoints?: { x: string, y: number }[];
  labelsX: string[] = [];
  dataY: number[] = [];

  ngOnChanges(changes: SimpleChanges) {
    this.drawGraph()
    //this.countuAssessmentSummaryBarChartDataPoints = changes['countuAssessmentSummaryBarChartDataPoints'].currentValue
  }
  

  ngOnInit(): void{
    this.drawGraph()
  }
  drawGraph(): void{
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');
    
    this.countuAssessmentSummaryBarChartDataPoints?.forEach(d => {
      this.labelsX.push(d.x);
      this.dataY.push(d.y);
    });

    this.basicData = {
      labels: this.labelsX,
      datasets: [
        {
          label: 'Score attained',
          data: this.dataY,
          backgroundColor: ['rgba(255, 159, 64, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(153, 102, 255, 0.2)'],
          borderColor: ['rgb(255, 159, 64)', 'rgb(75, 192, 192)', 'rgb(54, 162, 235)', 'rgb(153, 102, 255)'],
          borderWidth: 1
        }
      ]
    };

    this.basicOptions = {
      plugins: {
        legend: {
          labels: {
            color: textColor
          }
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false
          }
        },
        x: {
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
