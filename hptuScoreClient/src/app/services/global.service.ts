import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class GlobalService {
  API_HOST = 'hptu_app';
  API_PORT = '8082';
  BASE_API_URL: string = `http://${this.API_HOST}:${this.API_PORT}/hptu/api/v1`;
  BASE_API_URL_V2: string = `http://${this.API_HOST}:${this.API_PORT}/hptu/api/v2`;
  BASE_KEY_CLOAK_URL: string = 'http://localhost:8081/auth';
  KEY_CLOAK_CLIENT_ID: string = 'front-end-client-id';
  KEY_CLOAK_REALM: string = 'hptu-score-realm';
 
  HTTP_OPTIONS = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  constructor(public _snackBar: MatSnackBar) { }


  public handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 2000,
      verticalPosition: 'top'
    });
  }

  assessmentQuarters(): {value: string, display: string}[]{
    return [{value:'Q1', display: 'Jan - Mar'}, {value:'Q2', display: 'Apr - June'}, {value:'Q3', display: 'Jul - Sep'}, {value:'Q4', display: 'Oct - Dec'}];
  }

  assessmentYears(): {value: string, display: string}[]{
    return [
      {value:'2023', display: '2023'}, {value:'2024', display: '2024'}, {value:'2025', display: '2025'}, {value:'2026', display: '2026'}, 
    {value:'2027', display: '2027'}, {value:'2028', display: '2028'}, {value:'2029', display: '2029'}, {value:'2030', display: '2030'}
    ];
  }
}