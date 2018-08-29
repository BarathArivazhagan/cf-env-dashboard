import { Component, OnInit } from '@angular/core';
import { DataCenterService } from '../../services/data-center.service';
import { forkJoin } from "rxjs/observable/forkJoin";
import { ApplicationService } from '../../services/application.service';
import { UserDefinedServiceInstancesService } from '../../services/user-defined-service-instances.service';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { Router } from '@angular/router';
import { ActionService } from '../../services/action.service';


@Component({
  selector: 'app-datacenter',
  templateUrl: './datacenter.component.html',
  styleUrls: ['./datacenter.component.css']
})
export class DatacenterComponent implements OnInit {


  public appsGridApi;
  public appsGridColumnApi;
  public servicesGridApi;
  public servicesGridColumnApi;
  public datacenterName = '';
  public appsColumnDefs = [];
  public appsRowData = [];
  public servicesColumnDefs = [];
  public servicesRowData = [];
  public datacenters = [];
  public spaces= [];
  public orgs = [];
  public showOrgSpace = false;



  constructor(private dataCenterService: DataCenterService
  , private appsService: ApplicationService,
   private cupsService: UserDefinedServiceInstancesService,
   private spinnerService: Ng4LoadingSpinnerService
  , private router: Router,
    private actionService: ActionService) { }

  ngOnInit() {

    
    this.spinnerService.show();
    this.appsColumnDefs = [
        {headerName: 'Name', field: 'name', width: 200,
          cellClass: "cell-wrap-text",  autoHeight: true},
        {headerName: 'Org', field: 'org' , width: 150,
        cellClass: "cell-wrap-text",  autoHeight: true},
        {headerName: 'Space', field: 'space' , width: 150,
        cellClass: "cell-wrap-text",  autoHeight: true},
        {headerName: 'Instances', field: 'instances' , width: 150,
        cellClass: "cell-wrap-text",  autoHeight: true},
        {headerName: 'Status', field: 'status', width: 150,
        cellClass: "cell-wrap-text",  autoHeight: true},
        { headerName: 'Actions',
        suppressMenu: true,
        suppressSorting: true,
        cellRenderer: this.actionCellRenderer,        
        autoHeight: true,
        width: 375
       },
       {headerName: 'Memory', field: 'memory', width: 100,
       cellClass: "cell-wrap-text",  autoHeight: true},
       {headerName: 'Routes', field: 'routes', width: 150,
       cellClass: "cell-wrap-text",  autoHeight: true},
    ];

    this.servicesColumnDefs = [    
      {headerName: 'Name', field: 'name' },
      {headerName: 'Type', field: 'type'},
      {headerName: 'Org', field: 'org' , width: 100},
      {headerName: 'Space', field: 'space' , width: 100},
      { headerName: 'Actions',
      suppressMenu: true,
      suppressSorting: true,
      cellRenderer: this.serviceActionsCellRenderer,      
      autoHeight: true
    }
  ];
    
    this.datacenterName = this.dataCenterService.getDatacenter();
    console.log('datacenter name',this.datacenterName);
    if(this.datacenterName !=null && this.datacenterName !== undefined && this.datacenterName !== '') {
      this.view_data_center();
    }
    
  

    if(this.datacenterName !==undefined) {
      this.populateDropDowns();
    }
   
  }

  public populateDropDowns(): void {

    this.dataCenterService.getDataCenters()
      .subscribe( data => {
         console.log('datacenters', data);
         this.datacenters = data;
      }, err => {
         console.log(' error in getting datacenters');
      });
    
    this.dataCenterService.getOrgs(this.datacenterName)
      .subscribe( data =>{
        console.log('orgs', data);
        this.orgs = data;
       
      } , err =>{
        console.log(' error in getting datacenters');
      });

       
    this.dataCenterService.getSpaces(this.datacenterName)
    .subscribe( data =>{
      console.log('spaces', data);
      this.spaces = data;
     
    } , err =>{
      console.log(' error in getting datacenters');
    });


    

  }

  public view_data_center(): void {

    this.spinnerService.show();
    forkJoin([ this.appsService.getAppsbyDatacenter(this.datacenterName), this.cupsService.getCupsByDatacenter(this.datacenterName)])
          .subscribe( results => {
            console.log("results",results);
            let appRecords = [];
            let serviceRecords = [];
            const apps = results[0];
            const keys = Object.keys(apps);
            keys.forEach( key =>{
                console.log('org',key);
                const spaces =apps[key];
                console.log('spaces',spaces);
                const spacekeys = Object.keys(spaces);
                spacekeys.forEach( spaceKey =>{

                  const apps =spaces[spaceKey];
                  console.log('app summary',apps);
                  apps.forEach( app => {
                    appRecords.push({
                      'name' : app['name'],
                      'org' : key,
                      'space': spaceKey,
                      'instances' :app['instances'] ,
                      'status' : app['requestedState'],
                      'memory': app['memoryLimit'],
                      'routes' : app['urls']
                    });
                  });
                 
                });
            });
            console.log('app records',appRecords);
            this.appsRowData = appRecords;
          // service data
          const services = results[1];
          const serviceKeys = Object.keys(services);
          serviceKeys.forEach( serviceKey =>{
              console.log('org',serviceKey);
              const serviceSpaces =services[serviceKey];
              console.log('spaces',serviceSpaces);
              const spaceServiceKeys = Object.keys(serviceSpaces);
              spaceServiceKeys.forEach( spaceServiceKey =>{

                const services =serviceSpaces[spaceServiceKey];
                console.log('service summary',services);
                services.forEach( service => {
                  serviceRecords.push({
                    'name' : service['name'],
                    'org' : serviceKey,
                    'space': spaceServiceKey,
                    'type' : service['type']                   
                  });
                });
               
              });
          });
          console.log('service records',serviceRecords);
          this.servicesRowData = serviceRecords;
          this.spinnerService.hide();
          }, err => {
            console.log("error in get apps and services");
          });

  }

  onAppsGridReady(params) {
    this.appsGridApi = params.api;
    this.appsGridColumnApi = params.columnApi;
    params.api.sizeColumnsToFit();
    setTimeout(function() {
      params.api.resetRowHeights();
    }, 500);

  }

  onServicesGridReady(params) {

    this.servicesGridApi = params.api;
    this.servicesGridColumnApi = params.columnApi;
    params.api.sizeColumnsToFit();
    setTimeout(function() {
      params.api.resetRowHeights();
    }, 500);
  }

  public actionCellRenderer( params : any): string {
    console.log('action cell rendered invoked with params', params);
    return '<div class="btn-group"><button type="button" data-action-type="start" class="btn btn-success"> Start </button>'+
    '<button type="button" data-action-type="stop" class="btn btn-danger "> Stop </button></div>'+
    '<div class="btn-group"><button type="button" data-action-type="restart" class="btn btn-info "> Restart </button>'+
    '<button type="button" data-action-type="restage" class="btn btn-primary "> Restage </button></div>';
  }

  public serviceActionsCellRenderer(params : any) : string {
    console.log('services action cell rendered invoked with params', params);
    return '<div class="btn-group"><button type="button" data-action-type="view" class="btn btn-success"> View </button><button type="button" data-action-type="modify" class="btn btn-primary"> Modify </button>'+
           '<button type="button" data-action-type="bulkmodify" class="btn btn-primary"> Bulk Modify </button></div>';
  }

  public doOnChangeDropdown(action: string,value: string) {
    console.log("dropdown function called with action {} and value {} ",action,value);
    switch(action){

      case 'datacenter' : this.datacenterName = value;
                          this.view_data_center();break;

      
    }
  }


  public onRowClicked(e) {
    if (e.event.target !== undefined) {
        let data = e.data;
        let actionType = e.event.target.getAttribute('data-action-type');

        switch(actionType) {
            case "view":
                return this.onActionViewClick(data); 
            case "modify":
                return this.onActionModifyClick(data); 
            case "start":
                return this.onActionStartClick(data); 
            case "stop":
                return this.onActionStopClick(data); 
           case "restart":
                return this.onActionReStartClick(data);
            case "restage":
                return this.onActionRestageClick(data); 
           case 'bulkmodify' :
                return this.onActionBulkModifyClick(data);
        }
    }
  }

  public onActionBulkModifyClick(data : any) {

  }

  public onActionViewClick(data : any) {
    console.log('action view called with data {}',data);
    const serviceName = data.name;
    this.dataCenterService.setOrg(data.org);
    this.dataCenterService.setSpace(data.space);
    this.dataCenterService.setDatacenter(this.datacenterName);
    this.router.navigate(['/dashboard/cups', serviceName]);

  }

  public onActionModifyClick(data : any) {
    console.log('modify action with data ',data);
   
  }

  public onActionStartClick(data : any) {
    console.log('start action with data ',data);
    if(this.isActionAllowed(data.org,data.space,data.name)) {
      this.actionService.startApp(this.datacenterName,data.org,data.space,data.name)
                .subscribe( res => {
                  console.log(' started app with appName',data.name);
                }, err => {
                  console.error(' failed in starting app with appName',data.name);
                });
    }
  }

  public onActionStopClick(data : any) {
    console.log('stop action with data ',data);
    if(this.isActionAllowed(data.org,data.space,data.name)) {
      this.actionService.stopApp(this.datacenterName,data.org,data.space,data.name)
            .subscribe( res => {
              console.log(' stoped app with appName',data.name);
            }, err => {
              console.error(' failed in stopping app with appName',data.name);
            });
    }
  }

  public onActionReStartClick(data : any){
    console.log('restart action with data ',data);
    if(this.isActionAllowed(data.org,data.space,data.name)) {
      this.actionService.restartApp(this.datacenterName,data.org,data.space,data.name)
      .subscribe( res => {
        console.log(' restarted app with appName',data.name);
      }, err => {
        console.error(' failed in restarting app with appName',data.name);
      });
    }
  }

  public onActionRestageClick(data : any){
    console.log('restage action with data ',data);
    if(this.isActionAllowed(data.org,data.space,data.name)) {
      this.actionService.restageApp(this.datacenterName,data.org,data.space,data.name)
      .subscribe( res => {
        console.log(' restaged app with appName',data.name);
      }, err => {
        console.error(' failed in restaging app with appName',data.name);
      });
    }
  }

  private isActionAllowed( org: string, space: string, appName: string): boolean {
    if( org && space && appName) {
      return true;
    }else{
      console.error('action not allowed as criterias are not met ',appName);
      return false;
    }
  }

}
