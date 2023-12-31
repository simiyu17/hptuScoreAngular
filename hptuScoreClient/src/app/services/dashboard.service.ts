import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private httpClient: HttpClient, private globalService: GlobalService) { }

  getCountyAssessmentSummary(countyCode?: string, assessmentQuarter?: string, assessmentYear?: string): Observable<any> {
    if(countyCode && assessmentQuarter && assessmentYear){
      return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/county-assessments-summary?countyCode=${countyCode}&assessmentQuarter=${assessmentQuarter}&assessmentYear=${assessmentYear}`);
    }
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/county-assessments-summary`);
  }

  getCountyAssessmentSummaryBarDataPoints(countyCode?: string, assessmentQuarter?: string, assessmentYear?: string): Observable<any> {
    if(countyCode && assessmentQuarter && assessmentYear){
      return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/county-assessments-bar-data-points?countyCode=${countyCode}&assessmentQuarter=${assessmentQuarter}&assessmentYear=${assessmentYear}`);
    }
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/report/county-assessments-bar-data-points`);
  }
}
