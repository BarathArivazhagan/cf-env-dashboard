import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DatacenterComponent } from './components/datacenter/datacenter.component';
import { ApplicationComponent } from './components/application/application.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent  },
  { path: 'dashboard/:datacenter', component: DashboardComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CloudFoundryRoutingModule { }
