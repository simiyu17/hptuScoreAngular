import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { Observable } from 'rxjs';
import { QuestionSummaryDto } from '../dto/QuestionSummaryDto';

@Injectable({
  providedIn: 'root'
})
export class QuestionsService {

  constructor(private http: HttpClient, private globalService: GlobalService) {}

  getQuestions(): Observable<QuestionSummaryDto[]> {
    return this.http.get<QuestionSummaryDto[]>(`${this.globalService.BASE_API_URL}/hptu-questions`);
  }

  addQuestion(questionSummaryForm: QuestionSummaryDto, functionalityId: any): Observable<QuestionSummaryDto> {
    return this.http.post<QuestionSummaryDto>(`${this.globalService.BASE_API_URL}/hptu-questions/${functionalityId}`, questionSummaryForm);
  }

}
