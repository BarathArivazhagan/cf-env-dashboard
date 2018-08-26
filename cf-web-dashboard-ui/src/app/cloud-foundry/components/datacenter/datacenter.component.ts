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
        {headerName: 'Instances', field: 'instances' , width: 100},
        {headerName: 'Status', field: 'requestedState', width: 100},
        { headerName: 'Actions',
        suppressMenu: true,
        suppressSorting: true,
        cellRenderer: this.actionCellRenderer,
        autoWidth: true,
        autoHeight: true,
        width: 250
       },
       {headerName: 'Memory', field: 'memoryLimit', width: 100},
       {headerName: 'Routes', field: 'urls'},
    ];

    this.servicesColumnDefs = [
      {headerName: '#', field: 'index' },
      {headerName: 'Name', field: 'name' },
      {headerName: 'Status', field: 'status'},
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
      
    }
    

    this.appsRowData  = [
      { index: '1', name: 'Celica', status: 'STARTED' },
      { index: '2', name: 'Mondeo', status: 'STARTED' },
      { index: '3', name: 'Boxter', status: 'STARTED' }
    ];

   

    this.servicesRowData  = [
      { index: '1', name: 'Celica', status: 'STARTED' },
      { index: '2', name: 'Mondeo', status: 'STARTED' },
      { index: '3', name: 'Boxter', status: 'STARTED' }
    ];

    this.populateDropDowns();
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
            let apps = [];
            this.appsRowData = results[0];
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
    return '<button type="button" data-action-type="modify" class="btn btn-primary"> Modify </button>';
  }

  public doOnChangeDropdown(value: string) {
    console.log("dropdown function called ",value);
  }

}
