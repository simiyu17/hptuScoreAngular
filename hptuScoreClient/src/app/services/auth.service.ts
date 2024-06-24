import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserRole } from '../models/UserRole';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CountyAssessmentMetaData } from '../models/CountyAssessmentMetaData';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  USER_ROLE: UserRole = UserRole.NONE;
  AUTH_TOKEN_KEY: string = 'auth_token';
  USER_DETAILS_KEY: string = 'user_details';
  FORCE_PASS_CHANGE: string = 'force_change_password';
  jwtService: JwtHelperService = new JwtHelperService();

  constructor(private httpClient: HttpClient, private globalService: GlobalService, private router: Router) { }

  login = (authRequest: {username: string, password: string}): Observable<any> => this.httpClient.post(`${this.globalService.BASE_API_URL}/users/authenticate`, JSON.stringify(authRequest))

  public setLocalStorageValue = (key : string, value: any): void => localStorage.setItem(key, value)

  public getLocalStorageValue = (key: string): string | null => localStorage.getItem(key);

  public getAuthToken = (): string | null => this.getLocalStorageValue(this.AUTH_TOKEN_KEY);

  public clearLocalStorageValue = (): void => localStorage.clear();
  
  public decodeAuthToken = (): any =>  this.getAuthToken() === null ? null : this.jwtService.decodeToken(this.getAuthToken()!);

  public storeUserDetails = (token?: string) : void => {
    if(token){
      this.setLocalStorageValue(this.AUTH_TOKEN_KEY, token);
      this.setLocalStorageValue(this.USER_DETAILS_KEY, this.decodeAuthToken()['user']);
      this.setLocalStorageValue(this.FORCE_PASS_CHANGE, this.decodeAuthToken()[this.FORCE_PASS_CHANGE]);
    }
  };

  public isAuthenticated = () : boolean => this.getAuthToken() !== null && !this.jwtService.isTokenExpired(this.getAuthToken());

  public isUserAdmin(): boolean {
    return (this.decodeAuthToken()['roles'] as Array<string>).includes('Admin');
  }

  userRedirection(): void {
    if(this.isAuthenticated()){
      this.router.navigateByUrl("/dashboard");
    }else {
      this.doLogout();
    }
  }

  redirectToChangePassword(): void{
    this.router.navigateByUrl("/change-password");
  }


  doLogout(): void {
    this.clearLocalStorageValue();
    this.router.navigateByUrl("/login");
  }


  public storeUserCurrentDashBoardFilters = (ass: CountyAssessmentMetaData) : void => {
    if(ass){
      this.setLocalStorageValue('assessmentYearDashBoardFilter', ass.assessmentYear);
      this.setLocalStorageValue('assessmentQuarterDashBoardFilter', ass.assessmentQuarter);
      this.setLocalStorageValue('countyCodeDashBoardFilter', ass.countyCode);
    }
  };

  public retrieveUserCurrentDashBoardFilters = () : CountyAssessmentMetaData => {
    return {
      assessmentYear: this.getLocalStorageValue('assessmentYearDashBoardFilter'), 
      assessmentQuarter: this.getLocalStorageValue('assessmentQuarterDashBoardFilter'), 
      countyCode: this.getLocalStorageValue('countyCodeDashBoardFilter'), 
      assessmentLevel: null
    }
  };
}
