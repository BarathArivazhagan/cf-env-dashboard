import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CONSTANTS } from '../../constants/constants';
import { Observable } from 'rxjs';

@Injectable()
export class UserDefinedServiceInstancesService {

  private userServiceName;

  constructor(private _http: HttpClient) { }

  public getCups( datacenter: string , org: string, space: string): Observable<any> {

    return this._http.get(CONSTANTS.GET_SERVICES_API.concat("/").concat(datacenter).concat("/").concat(org).concat("/").concat(space));
  }

  public getCupsByDatacenter( datacenter: string) : Observable<any> {
    return this._http.get(CONSTANTS.GET_SERVICES_API.concat("/").concat(datacenter));
  }

  public getCupsByServiceName( datacenter: string, org: string, space: string, serviceName: string): Observable<any> {
    return this._http.get(CONSTANTS.GET_SERVICES_API
      .concat("/").concat(datacenter).concat("/").concat(org).concat("/").concat(space)
      .concat("/").concat(serviceName));
  }

  public updateCupsByServiceName( datacenter: string, org: string, space: string, serviceName: string, credentials: any): Observable<any> {
    
    return this._http.put(CONSTANTS.GET_SERVICES_API
      .concat("/").concat(datacenter).concat("/").concat(org).concat("/").concat(space)
      .concat("/").concat(serviceName), credentials);
  }

  public setUserDefinedServiceName( serviceName: string ) {
    this.userServiceName = serviceName;
  }

  public getUserDefinedServiceName() {
    return this.userServiceName;
  }

  

}
