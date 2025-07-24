import { Routes } from '@angular/router';
import { LoginComponent } from '../login/login.component';
import { RegisterComponent } from '../register/register.component';
import { OauthComponent } from '../oauth/oauth.component';
import { ValidationComponent } from '../validation/validation.component';

export const AUTH_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'oauth-success',
    component: OauthComponent
  },
  {
    path: 'validation',
    component: ValidationComponent
  }
];
