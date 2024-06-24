import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FunctionalityDto } from '../dto/FunctionalityDto';
import { GlobalService } from './global.service';

@Injectable({
  providedIn: 'root'
})
export class FunctionalityService {

  constructor(private http: HttpClient, private globalService: GlobalService) {}

  getFunctionalities(): Observable<FunctionalityDto[]> {
    return this.http.get<FunctionalityDto[]>(`${this.globalService.BASE_API_URL}/hptu-functionalities`);
  }

  addFunctionality(functionality: FunctionalityDto): Observable<FunctionalityDto> {
    return this.http.post<FunctionalityDto>(`${this.globalService.BASE_API_URL}/hptu-functionalities`, functionality);
  }

  updateFunctionality(functionality: FunctionalityDto): Observable<FunctionalityDto> {
    return this.http.put<FunctionalityDto>(`${this.globalService.BASE_API_URL}/hptu-functionalities/${functionality.id}`, functionality);
  }

  deleteFunctionality(id: number): Observable<void> {
    return this.http.delete<void>(`${this.globalService.BASE_API_URL}/hptu-functionalities/${id}`);
  }
}
