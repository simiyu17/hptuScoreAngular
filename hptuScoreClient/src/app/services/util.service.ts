import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { CountyAssessmentMetaData } from '../models/CountyAssessmentMetaData';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  private filteredAssessment: BehaviorSubject<any> = new BehaviorSubject(undefined);

  constructor(private httpClient: HttpClient, private globalService: GlobalService) { }

  getAllKenyanCounties = (): Observable<any> => this.httpClient.get(`${this.globalService.BASE_API_URL}/report/counties`);

  currentAssessmentData = (): Observable<CountyAssessmentMetaData> => this.filteredAssessment.asObservable();

  onAssessmentDataReceived = (newAssessment?: CountyAssessmentMetaData) => this.filteredAssessment.next(newAssessment);

}
