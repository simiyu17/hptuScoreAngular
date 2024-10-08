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

  getCountyAssessmentSummaryByPillar = (assessment: CountyAssessmentMetaData | null, pillarName: string | null): Observable<any> => {
    if(!assessment){
      return this.httpClient.get(`${this.globalService.BASE_API_URL_V2}/reports/county-assessments-summary`);
    }
    let queryParams = `countyCode=${assessment?.countyCode}&assessmentQuarter=${assessment?.assessmentQuarter}&assessmentYear=${assessment?.assessmentYear}`
    if(pillarName){
      queryParams = `${queryParams}&pillarName=${pillarName}`
    }
    return this.httpClient.get(`${this.globalService.BASE_API_URL_V2}/reports/county-assessments-summary?${queryParams}`);
  }

  getCountyAssessmentSummaryByCategory(metaDataId: number, pillarName: string): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL_V2}/reports/assessments-summary-per-pillar/${metaDataId}/${pillarName}`);
  }

  exportCountyAssessmentSummaryToExcel(metaDataId?: number): Observable<any> {
    if(metaDataId){
      return this.httpClient.get(`${this.globalService.BASE_API_URL_V2}/reports/export-to-excel?metaDataId=${metaDataId}&analysisByPillarTableTitle=Summary`);
    }
    return of(null)
    
  }

  exportCountyAssessmentSummaryToExcelV2(assessment?: CountyAssessmentMetaData): Observable<any> {
    if(!assessment){
      return this.httpClient.get(`${this.globalService.BASE_API_URL_V2}/reports/county-assessments-summary-excel`);
    }
    return this.httpClient.get(`${this.globalService.BASE_API_URL_V2}/reports/county-assessments-summary-excel?countyCode=${assessment?.countyCode}&assessmentQuarter=${assessment?.assessmentQuarter}&assessmentYear=${assessment?.assessmentYear}`);
  }

  getHPTUCountyAssessmentSummary = (countyCode: string | null, assessmentDate: string | null, functionalityName: string | null): Observable<any> => {
    if(!countyCode){
      return this.httpClient.get(`${this.globalService.BASE_API_URL_V2}/reports/county-hptu-assessments-summary`);
    }
    let queryParams = `countyCode=${countyCode}`
    if(assessmentDate){
      queryParams = `${queryParams}&assessmentDate=${assessmentDate}`
    }
    if(functionalityName){
      queryParams = `${queryParams}&functionalityName=${functionalityName}`
    }
   
    return this.httpClient.get(`${this.globalService.BASE_API_URL_V2}/reports/county-hptu-assessments-summary?${queryParams}`);
  }
}
