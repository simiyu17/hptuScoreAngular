import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private httpClient: HttpClient, private globalService: GlobalService) { }

  getCountyAssessmentSummaryByPillar(countyCode?: string, assessmentQuarter?: string, assessmentYear?: string): Observable<any> {
    if(countyCode && assessmentQuarter && assessmentYear){
      return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/county-assessments-summary?countyCode=${countyCode}&assessmentQuarter=${assessmentQuarter}&assessmentYear=${assessmentYear}`);
    }
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/county-assessments-summary`);
  }

  getCountyAssessmentSummaryByCategory(metaDataId: number, pillarName: string): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/assessments-summary-per-pillar/${metaDataId}/${pillarName}`);
  }

  exportCountyAssessmentSummaryToExcel(metaDataId?: number): Observable<any> {
    if(metaDataId){
      return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/export-to-excel?metaDataId=${metaDataId}&countyName=Nairobi&analysisByPillarTableTitle=FuckYou`);
    }
    return of(null)
    
  }
}
