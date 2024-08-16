import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import {MatCardModule} from '@angular/material/card';

@Component({
  selector: 'app-hptu-assessment-graph',
  standalone: true,
  imports: [ChartModule, MatCardModule],
  templateUrl: './hptu-assessment-graph.component.html',
  styleUrl: './hptu-assessment-graph.component.scss'
})
export class HptuAssessmentGraphComponent implements OnInit, OnChanges {

  basicData: any;

  basicOptions: any;

  @Input() countuAssessmentSummaryBarChartDataPoints?: { x: string, y: number }[];
  labelsX: string[] = [];
  dataY: number[] = [];
  backgroundColorArray: string[] = [];
  borderColorArray: string[] = [];

  ngOnChanges(changes: SimpleChanges) {
    this.countuAssessmentSummaryBarChartDataPoints = changes['countuAssessmentSummaryBarChartDataPoints'].currentValue
    this.drawGraph()
  }
  

  ngOnInit(): void{
    this.drawGraph()
  }
  drawGraph(): void{
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.labelsX = [];
    this.dataY = [];
    this.backgroundColorArray = [];
    this.borderColorArray = [];
    
    this.countuAssessmentSummaryBarChartDataPoints?.forEach(d => {
      this.labelsX.push(d.x);
      this.dataY.push(d.y);
      if (d.y >= 80){
        this.backgroundColorArray.push('rgba(75, 192, 192, 0.2)');
        this.borderColorArray.push('rgb(75, 192, 192)');
    } else if (d.y >= 65) {
        this.backgroundColorArray.push('rgba(54, 162, 235, 0.2)');
        this.borderColorArray.push('rgb(54, 162, 235)');
    }else if (d.y >= 50) {
        this.backgroundColorArray.push('rgba(153, 102, 255, 0.2)');
        this.borderColorArray.push('rgb(153, 102, 255)');
    }else {
        this.backgroundColorArray.push('rgba(255, 159, 64, 0.2)');
        this.borderColorArray.push('rgb(255, 159, 64)');
    }
    });

    this.basicData = {
      labels: this.labelsX,
      datasets: [
        {
          label: 'Score attained',
          data: this.dataY,
          backgroundColor: this.backgroundColorArray,
          borderColor: this.borderColorArray,
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
