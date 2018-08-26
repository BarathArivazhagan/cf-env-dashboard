import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs/Observable';
import { CONSTANTS } from '../../constants/constants';

@Injectable()
export class DataCenterService {


  private datacenter: string;

  constructor(private _http: HttpClient) { }

  public getDataCenters(): Observable<any> {

    return this._http.get(CONSTANTS.GET_DATACENTERS_API);
  }

  public getOrgs( datacenter: string): Observable<any> {
    return this._http.get(CONSTANTS.GET_ORGS_API.concat('/').concat(datacenter));
  }

  public getSpaces( datacenter: string): Observable<any> {
    return this._http.get(CONSTANTS.GET_SPACES_API.concat('/').concat(datacenter));
  }

  public setDatacenter( datacenter: string) : void {
      this.datacenter = datacenter;
  }

  public getDatacenter() : string {
    return this.datacenter;
  }

}
