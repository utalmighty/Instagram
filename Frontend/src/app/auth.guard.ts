import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { InstagramServiceService } from './services/instagram-service.service';
import { profile } from './props/profile';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  user!: profile;

  constructor(private service: InstagramServiceService, private router: Router) {
    service.loginUser$.subscribe((a) => this.user = a);
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.user.userId) return true;
    this.router.navigate(['/login']);
    return false;
  }

}
