import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { CountyAssessmentDto } from '../dto/CountyAssessmentDto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CountyAssessmentService {

  constructor(private httpClient: HttpClient, private globalService: GlobalService) { }

  createCountyAssessment(assessment: CountyAssessmentDto): Observable<any> {
    return this.httpClient.post(`${this.globalService.BASE_API_URL}/county-assessments`, JSON.stringify(assessment));
  }
}
