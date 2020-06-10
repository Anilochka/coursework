import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class NotLoggedInGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(): boolean {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['login']).finally();
    }

    return this.authService.isLoggedIn();
  }

}
