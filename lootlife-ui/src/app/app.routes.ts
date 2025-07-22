import { Routes } from '@angular/router';
import { MainComponent } from './features/main/main.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { AuthComponent } from './features/auth/auth/auth.component';
import { AUTH_ROUTES } from './features/auth/auth/auth.routes';
import { ProfileComponent } from './features/profile/profile.component';
import { TaskComponent } from './features/task/task.component';
import { MissionComponent } from './features/mission/mission.component';

export const routes: Routes = [
  { path: '', component: MainComponent},
  {
    path: 'auth',
    component: AuthComponent,
    children: AUTH_ROUTES
  },
  { path: 'profile', component: ProfileComponent},
  { path: 'task', component: TaskComponent},
  { path: 'mission', component: MissionComponent}
];
