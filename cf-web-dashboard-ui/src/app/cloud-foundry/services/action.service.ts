import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CONSTANTS } from '../../constants/constants';

@Injectable()
export class ActionService {

  constructor(private _http: HttpClient) { }

  public startApp( datacenter: string, org: string, space: string, appName: string): Observable<any> {
    return this._http.post(CONSTANTS.START_ACTION_API.concat(this.appendPath(datacenter,org,space,appName)),null);
  }

  public stopApp( datacenter: string, org: string, space: string, appName: string): Observable<any> {
    return this._http.post(CONSTANTS.STOP_ACTION_API.concat(this.appendPath(datacenter,org,space,appName)),null);
  }

  public restartApp( datacenter: string, org: string, space: string, appName: string): Observable<any> {
    return this._http.post(CONSTANTS.RESTART_ACTION_API.concat(this.appendPath(datacenter,org,space,appName)), null);
  }

  public restageApp( datacenter: string, org: string, space: string, appName: string): Observable<any> {
    return this._http.post(CONSTANTS.RESTAGE_ACTION_API.concat(this.appendPath(datacenter,org,space,appName)), null);
  }



  public appendPath(datacenter: string, org: string, space: string, appName: string): string {

    return "/".concat(datacenter).concat("/").concat(org).concat("/").concat(space).concat("/").concat(appName);
  }

}
