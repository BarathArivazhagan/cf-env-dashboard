import { Component, OnInit } from '@angular/core';
import { UserDefinedServiceInstancesService } from '../../services/user-defined-service-instances.service';
import { DataCenterService } from '../../services/data-center.service';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-user-defined-service',
  templateUrl: './user-defined-service.component.html',
  styleUrls: ['./user-defined-service.component.css']
})
export class UserDefinedServiceComponent implements OnInit {

  public userServiceName:string ;
  public isBulk = false;
  public credentials = [] ;

  constructor( private userDefinedService: UserDefinedServiceInstancesService,
               private datacenterService: DataCenterService,
               private spinnerService: Ng4LoadingSpinnerService ) { }

  ngOnInit() {

    this.spinnerService.show();
    this.userServiceName = this.userDefinedService.getUserDefinedServiceName();
    console.log('serive name {}',this.userServiceName);
    const datacenter = this.datacenterService.getDatacenter();
    const org = this.datacenterService.getOrg();
    const space = this.datacenterService.getSpace();
    this.userDefinedService.getCupsByServiceName(datacenter,org,space,this.userServiceName)
        .subscribe( data => {
          console.log('service details with service name {} and details {}',this.userServiceName,data);
          this.credentials = data.entity.credentials;
        }, err => {
          console.log('error in getting the service details {}',err);
        });


  }

  public closeItem( key: string) {
    console.log('key to be removed $key',key);
  }

  public updateService(): void {
    console.log('update service');
  }

}
