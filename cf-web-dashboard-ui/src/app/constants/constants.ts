import { environment } from '../../environments/environment';

export class CONSTANTS {

    public static SUMMARY_API = environment.serviceUrl.concat('/summary');
    public static GET_APPS_API = environment.serviceUrl.concat('/apps');
    public static GET_SERVICES_API = environment.serviceUrl.concat('/cups');
    public static GET_DATACENTERS_API = environment.serviceUrl.concat('/home/datacenters');
    public static GET_ORGS_API = environment.serviceUrl.concat('/home/orgs');
    public static GET_SPACES_API = environment.serviceUrl.concat('/home/spaces');

    public static START_ACTION_API = environment.serviceUrl.concat('/actions/start');
    public static RESTART_ACTION_API = environment.serviceUrl.concat('/actions/restart');
    public static STOP_ACTION_API = environment.serviceUrl.concat('/actions/stop');
    public static RESTAGE_ACTION_API = environment.serviceUrl.concat('/actions/restage');
    

}
