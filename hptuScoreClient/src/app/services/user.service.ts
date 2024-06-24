import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { Observable } from 'rxjs';
import { UserDto } from '../dto/UserDto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient, private globalService: GlobalService) { }

  getAvailableUsers(): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/users`);
  }

  createUser(user: UserDto): Observable<any> {
    return this.httpClient.post(`${this.globalService.BASE_API_URL}/users`, JSON.stringify(user));
  }

  updateUser(userId: number, user: UserDto): Observable<any> {
    return this.httpClient.put(`${this.globalService.BASE_API_URL}/users/${userId}`, JSON.stringify(user));
  }

  updateUserPassword(userPassDto: {password: string, newPass: string, passConfirm: string}): Observable<any> {
    return this.httpClient.put(`${this.globalService.BASE_API_URL}/users/change-password`, JSON.stringify(userPassDto));
  }

  getUserById(userId: number): Observable<any> {
    return this.httpClient.get(`${this.globalService.BASE_API_URL}/users/${userId}`);
  }
}
