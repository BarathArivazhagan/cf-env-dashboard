import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CloudFoundryRoutingModule } from './cloud-foundry-routing.module';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DatacenterComponent } from './components/datacenter/datacenter.component';
import { ApplicationComponent } from './components/application/application.component';
import { UserDefinedServiceComponent } from './components/user-defined-service/user-defined-service.component';
import { DataCenterService } from './services/data-center.service';
import { ApplicationService } from './services/application.service';
import { UserDefinedServiceInstancesService } from './services/user-defined-service-instances.service';
import { OverviewComponent } from './components/overview/overview.component';
import { RowOverviewComponent } from './components/row-overview/row-overview.component';
import { DashboardService } from './services/dashboard.service';
import { AgGridModule } from 'ag-grid-angular';
import { Ng4LoadingSpinnerModule } from 'ng4-loading-spinner';

@NgModule({
  imports: [
    CommonModule,
    CloudFoundryRoutingModule,
    AgGridModule.withComponents([DatacenterComponent]),
  ],
  declarations: [DashboardComponent, DatacenterComponent,
                 ApplicationComponent, UserDefinedServiceComponent,
                 OverviewComponent, RowOverviewComponent],
  providers: [ DataCenterService, ApplicationService, UserDefinedServiceInstancesService, DashboardService]
})
export class CloudFoundryModule { }
