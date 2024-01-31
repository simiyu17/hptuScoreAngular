import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';

export const AuthGuard: CanActivateFn = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    Observable<boolean | UrlTree> 
    | Promise<boolean | UrlTree> 
    | boolean 
    | UrlTree=> {
  
    return !!inject(AuthService).isAuthenticated();
  
  };