import { Routes } from '@angular/router';
import { MainComponent } from './features/main/main.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { AuthComponent } from './features/auth/auth/auth.component';
import { AUTH_ROUTES } from './features/auth/auth/auth.routes';

export const routes: Routes = [
  { path: '', component: MainComponent, pathMatch: 'full' },
  { path: 'register', component: RegisterComponent },
  {
    path: 'auth',
    component: AuthComponent,
    children: AUTH_ROUTES
  },
];
