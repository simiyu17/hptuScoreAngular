import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { Observable, of } from 'rxjs';
import { CountyAssessmentMetaData } from '../models/CountyAssessmentMetaData';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private httpClient: HttpClient, private globalService: GlobalService) { }

  getCountyAssessmentSummaryByPillar = (assessment?: CountyAssessmentMetaData): Observable<any> => {
    if(assessment && assessment.countyCode && assessment.assessmentQuarter && assessment.assessmentYear){
      return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/county-assessments-summary?countyCode=${assessment.countyCode}&assessmentQuarter=${assessment.assessmentQuarter}&assessmentYear=${assessment.assessmentYear}`);
    }
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/county-assessments-summary`);
  }

  getCountyAssessmentSummaryByCategory(metaDataId: number, pillarName: string): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/assessments-summary-per-pillar/${metaDataId}/${pillarName}`);
  }

  exportCountyAssessmentSummaryToExcel(metaDataId?: number): Observable<any> {
    if(metaDataId){
      return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/export-to-excel?metaDataId=${metaDataId}&analysisByPillarTableTitle=FuckYou`);
    }
    return of(null)
    
  }
}
