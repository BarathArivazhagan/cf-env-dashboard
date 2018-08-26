import { Component, OnInit } from '@angular/core';
import { DataCenterService } from '../../services/data-center.service';
import { forkJoin } from "rxjs/observable/forkJoin";
import { ApplicationService } from '../../services/application.service';
import { UserDefinedServiceInstancesService } from '../../services/user-defined-service-instances.service';


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
   private cupsService: UserDefinedServiceInstancesService) { }

  ngOnInit() {

    

    this.appsColumnDefs = [
        {headerName: 'Name', field: 'name', width: 150},
        {headerName: 'Org', field: 'org' , width: 100},
        {headerName: 'Space', field: 'space' , width: 100},
        {headerName: 'Instances', field: 'instances' , width: 100},
        {headerName: 'Status', field: 'status', width: 100},
        { headerName: 'Actions',
        suppressMenu: true,
        suppressSorting: true,
        cellRenderer: this.actionCellRenderer,
        autoWidth: true,
        autoHeight: true,
        width: 250
       },
       {headerName: 'Memory', field: 'memory', width: 100},
       {headerName: 'Routes', field: 'routes'},
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
      autoWidth: true,
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
          
          }, err => {
            console.log("error in get apps and services");
          });

  }

  onAppsGridReady(params) {
    this.appsGridApi = params.api;
    this.appsGridColumnApi = params.columnApi;
    params.api.sizeColumnsToFit();
  }

  onServicesGridReady(params) {

    this.servicesGridApi = params.api;
    this.servicesGridColumnApi = params.columnApi;
    params.api.sizeColumnsToFit();
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
    return '<div class="btn-group"><button type="button" data-action-type="view" class="btn btn-success"> View </button><button type="button" data-action-type="modify" class="btn btn-primary"> Modify </button></div>';
  }

  public doOnChangeDropdown(value: string) {
    console.log("dropdown function called ",value);
  }

}
