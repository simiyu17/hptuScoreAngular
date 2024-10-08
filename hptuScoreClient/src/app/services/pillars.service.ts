import { Injectable } from '@angular/core';
import { AssessmentPillar } from '../models/AssessmentPillar';
import { Observable } from 'rxjs';
import { GlobalService } from './global.service';
import { HttpClient } from '@angular/common/http';
import { PillarCategory } from '../models/PillarCategory';

@Injectable({
  providedIn: 'root'
})
export class PillarsService {


  constructor(private httpClient: HttpClient, private globalService: GlobalService) { }


  getAllPillars(): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/assessment-definitions`);
  }

  createPillar(pillar: AssessmentPillar): Observable<any> {
    return this.httpClient.post(`${this.globalService.BASE_API_URL}/assessment-definitions`, JSON.stringify(pillar));
  }

  updatePillarById(pillarId: number, udatedPillar: AssessmentPillar): Observable<any> {
    return this.httpClient.put(`${this.globalService.BASE_API_URL}/assessment-definitions/${pillarId}`, JSON.stringify(udatedPillar));
  }


  getPillarById(pillarId: number): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/assessment-definitions/${pillarId}`);
  }

  deletePillarById(pillarId: number): Observable<any> {
    return this.httpClient.delete(`${this.globalService.BASE_API_URL}/assessment-definitions/${pillarId}`);
  }





  getAllCategoriesByPillarId(pillarId: any, selectedQuarter: string | null): Observable<any> {
    if(selectedQuarter){
      return this.httpClient.get(`${this.globalService.BASE_API_URL}/assessment-definitions/${pillarId}/categories?quarter=${selectedQuarter}`);
    }
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/assessment-definitions/${pillarId}/categories`);
  }

  createPillarCategory(pillarId: number, category: PillarCategory): Observable<any> {
    return this.httpClient.post(`${this.globalService.BASE_API_URL}/assessment-definitions/${pillarId}/categories`, JSON.stringify(category));
  }

  updatePillarCategoryById(pillarId: number, categoryId: number, udatedCategory: PillarCategory): Observable<any> {
    return this.httpClient.put(`${this.globalService.BASE_API_URL}/assessment-definitions/${pillarId}/categories/${categoryId}`, JSON.stringify(udatedCategory));
  }


  getPillarCategoryById(pillarId: number, categoryId: number): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/assessment-definitions/${pillarId}/categories/${categoryId}`);
  }

  deletePillarCategoryById(pillarId: number, categoryId: number): Observable<any> {
    return this.httpClient.delete(`${this.globalService.BASE_API_URL}/assessment-definitions/${pillarId}/categories/${categoryId}`);
  }
}
