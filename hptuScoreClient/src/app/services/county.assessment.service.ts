import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { CountyAssessmentDto } from '../dto/CountyAssessmentDto';
import { Observable } from 'rxjs';
import { CountyAssessmentMetaData } from '../models/CountyAssessmentMetaData';

@Injectable({
  providedIn: 'root'
})
export class CountyAssessmentService {

  constructor(private httpClient: HttpClient, private globalService: GlobalService) { }

  getTopFiveCountyAssessments(): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/county-assessments`);
  }

  createCountyAssessment(assessment: CountyAssessmentDto): Observable<any> {
    return this.httpClient.post(`${this.globalService.BASE_API_URL}/county-assessments`, JSON.stringify(assessment));
  }

  getCountyAssessmentByCountyYearAndQuater(assessment: CountyAssessmentMetaData): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/county-assessments/find-one?countyCode=${assessment.countyCode}&assessmentYear=${assessment.assessmentYear}&assessmentQuarter=${assessment.assessmentQuarter}`);
  }

  getCountyAssessmentById(assessmentId: number): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/county-assessments/${assessmentId}`);
  }
}
