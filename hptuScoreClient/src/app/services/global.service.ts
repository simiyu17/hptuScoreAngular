import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class GlobalService {
  BASE_API_URL: string = 'http://localhost:8082/api/v1';
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
}