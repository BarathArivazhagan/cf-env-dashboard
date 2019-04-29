import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CONSTANTS } from '../../constants/constants';
import { Observable } from 'rxjs';

@Injectable()
export class DashboardService {

  constructor(private _http: HttpClient) { }


  public getSummary(): Observable<any> {

    return this._http.get(CONSTANTS.SUMMARY_API);
        
  }

}
