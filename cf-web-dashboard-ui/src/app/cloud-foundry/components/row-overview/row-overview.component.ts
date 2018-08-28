import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../../services/dashboard.service';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-row-overview',
  templateUrl: './row-overview.component.html',
  styleUrls: ['./row-overview.component.css']
})
export class RowOverviewComponent implements OnInit {
  temp = Array;
  math = Math;
  public datacenters: Object[];


  constructor(private _router: Router, private dashBoardService: DashboardService,
    private spinnerService: Ng4LoadingSpinnerService) { }

  ngOnInit() {

    this.spinnerService.show();
    this.datacenters = [];
    this.dashBoardService.getSummary()
      .subscribe(data => {
        console.log(data);
        if (data !== undefined && data != null) {
          this.bindSummary(data);
        }
      }, err => {
        console.log('error in getting the summary');
      });

  }

  public bindSummary(response: any): void {

    if (response['datacenters'] !== undefined) {
      const dcs: string[] = Object.keys(response['datacenters']);
      dcs.forEach(datacenter => {
        const organizations = response['organizations'][datacenter];
        const orgs = [];
        const apiHost = response['datacenters'][datacenter];
        console.log('orgs ', organizations);
        let spaces = [];
        if (organizations != null && organizations.length > 0) {
          organizations.forEach(org => {
            spaces = org.spaces;
            orgs.push(org.name);
          });
        }
        const applications = response['applications'][datacenter];
        const services = response['services'][datacenter];
        console.log('spaces', spaces);
        if (applications != null && applications !== undefined) { console.log('applications', JSON.stringify(applications)); }
        if (services != null && services !== undefined) { console.log('services', JSON.stringify(services)); }
        this.datacenters.push({
          'name': datacenter,
          'apiHost': apiHost,
          'orgs': orgs,
          'spaces': spaces,
          'applications': applications !== undefined ? applications : [],
          'services': services !== undefined ? services : []
        });
        this.spinnerService.hide();
      });
      console.log('summary', this.datacenters);
    }


  }

  public navigateToDatacenter(datacenter: string) {

    this.spinnerService.show();
    console.log('datacenter name', datacenter);
    this._router.navigate(['/dashboard', datacenter]);
  }



}
