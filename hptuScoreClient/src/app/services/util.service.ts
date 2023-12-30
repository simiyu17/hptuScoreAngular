import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor(private httpClient: HttpClient, private globalService: GlobalService) { }

  getAllKenyanCounties(): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/util/counties`);
  }
}
