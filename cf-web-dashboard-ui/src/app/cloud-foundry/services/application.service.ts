import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CONSTANTS } from '../../constants/constants';

@Injectable()
export class ApplicationService {

  constructor(private _http: HttpClient) { }

  public getApps( datacenter: string , org: string, space: string): Observable<any> {

    return this._http.get(CONSTANTS.GET_APPS_API.concat("/").concat(datacenter).concat("/").concat(org).concat("/").concat(space));
  }

  public getAppsbyDatacenter( datacenter: string) : Observable<any> {
    return this._http.get(CONSTANTS.GET_APPS_API.concat("/").concat(datacenter));
  }

}
