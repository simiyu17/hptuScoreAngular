import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserRole } from '../models/UserRole';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  USER_ROLE: UserRole = UserRole.NONE;
  AUTH_TOKEN_KEY: string = 'auth_token';
  USER_DETAILS_KEY: string = 'user_details';
  jwtService: JwtHelperService = new JwtHelperService();

  constructor(private httpClient: HttpClient, private globalService: GlobalService, private router: Router) { }

  login = (authRequest: {username: string, password: string}): Observable<any> => this.httpClient.post(`${this.globalService.BASE_API_URL}/users/authenticate`, JSON.stringify(authRequest))

  public setLocalStorageValue = (key : string, value: any): void => localStorage.setItem(key, value)

  public getAuthToken = (): string | null => localStorage.getItem(this.AUTH_TOKEN_KEY);

  public clearLocalStorageValue = (): void => localStorage.clear();
  
  public decodeAuthToken = (): any =>  this.getAuthToken() === null ? null : this.jwtService.decodeToken(this.getAuthToken()!);

  public storeUserDetails = (token?: string) : void => {
    if(token){
      this.setLocalStorageValue(this.AUTH_TOKEN_KEY, token);
      this.setLocalStorageValue(this.USER_DETAILS_KEY, this.decodeAuthToken()['user']);
    }
  };

  public isAuthenticated = () : boolean => this.getAuthToken() !== null && !this.jwtService.isTokenExpired(this.getAuthToken());

  public isUserAdmin(): boolean {
    return (this.decodeAuthToken()['groups'] as Array<string>).includes('Admin');
  }

  userRedirection(): void {
    if(this.isAuthenticated()){
      this.router.navigate(['']);
    }else {
      this.doLogout();
    }
  }


  doLogout(): void {
    this.clearLocalStorageValue();
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
    this.router.navigate(['login']));
  }
}
