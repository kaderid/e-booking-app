import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ClientDashboardComponent } from './components/client/dashboard/dashboard.component';
import { PrestataireDashboardComponent } from './components/prestataire/dashboard/dashboard.component';
import { AdminDashboardComponent } from './components/admin/dashboard/dashboard.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'client/dashboard',
    component: ClientDashboardComponent,
    canActivate: [AuthGuard],
    data: { role: 'CLIENT' }
  },
  {
    path: 'prestataire/dashboard',
    component: PrestataireDashboardComponent,
    canActivate: [AuthGuard],
    data: { role: 'PROVIDER' }
  },
  {
    path: 'admin/dashboard',
    component: AdminDashboardComponent,
    canActivate: [AuthGuard],
    data: { role: 'ADMIN' }
  },
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
