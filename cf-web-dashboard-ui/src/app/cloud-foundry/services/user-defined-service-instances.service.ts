import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CONSTANTS } from '../../constants/constants';
import { Observable } from 'rxjs';

@Injectable()
export class UserDefinedServiceInstancesService {

  constructor(private _http: HttpClient) { }

  public getCups( datacenter: string , org: string, space: string): Observable<any> {

    return this._http.get(CONSTANTS.GET_SERVICES_API.concat("/").concat(datacenter).concat("/").concat(org).concat("/").concat(space));
  }

  public getCupsByDatacenter( datacenter: string) : Observable<any> {
    return this._http.get(CONSTANTS.GET_SERVICES_API.concat("/").concat(datacenter));
  }

}
