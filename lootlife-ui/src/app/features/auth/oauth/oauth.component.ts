import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { UserService } from '../../../core/services/user.service';
import { AuthResponse } from '../../../models/auth-response.model';

@Component({
  selector: 'app-oauth',
  imports: [],
  templateUrl: './oauth.component.html',
  styleUrl: './oauth.component.css'
})
export class OauthComponent implements OnInit{

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private _authService: AuthService,
    private _userService: UserService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if (token) {
        alert(token);
        const authResponse: AuthResponse = {
          token: token
        }
        this._authService.saveToken(authResponse);
        this._userService.fetchUserInformation();
        this.router.navigate(['/']);
      } else {
        console.error('Token no encontrado en la URL');
        this.router.navigate(['/auth/login']);
      }
    });
  }

}
