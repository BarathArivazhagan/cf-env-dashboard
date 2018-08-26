package com.barath.app.cloudfoundry.service;

import com.barath.app.model.DashboardSummary;
import org.cloudfoundry.operations.applications.ApplicationSummary;
import org.cloudfoundry.operations.services.ServiceInstanceSummary;

import java.util.List;
import java.util.Map;

public interface DashboardService {

    DashboardSummary getDashboardSummary();
    Map<String, List<ApplicationSummary>> getApplications();
    Map<String, List<ServiceInstanceSummary>> getServices();
    List<ApplicationSummary> getApplicationsByDatacenter(String datacenter);
    List<ServiceInstanceSummary> getServicesByDatacenter(String datacenter);
    Map<String,Map<String,List<ApplicationSummary>>> groupAppsByOrgAndSpace(String datacenter);
    Map<String,Map<String,List<ServiceInstanceSummary>>> groupServicesByOrgAndSpace(String datacenter);


}
