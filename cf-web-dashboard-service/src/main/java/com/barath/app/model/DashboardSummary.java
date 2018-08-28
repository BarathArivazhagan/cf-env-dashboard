package com.barath.app.model;

import org.cloudfoundry.operations.applications.ApplicationSummary;
import org.cloudfoundry.operations.services.ServiceInstanceSummary;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DashboardSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String,String> datacenters;

    private Map<String, List<Organization>> organizations;

    private Map<String,List<ApplicationSummary>> applications;

    private Map<String,List<ServiceInstanceSummary>> services;


    public Map<String, String> getDatacenters() {
        return datacenters;
    }

    public void setDatacenters(Map<String, String> datacenters) {
        this.datacenters = datacenters;
    }

   
    public Map<String, List<ApplicationSummary>> getApplications() {
        return applications;
    }

    public void setApplications(Map<String, List<ApplicationSummary>> applications) {
        this.applications = applications;
    }

    public Map<String, List<ServiceInstanceSummary>> getServices() {
        return services;
    }

    public void setServices(Map<String, List<ServiceInstanceSummary>> services) {
        this.services = services;
    }
    
    

    public Map<String, List<Organization>> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Map<String, List<Organization>> organizations) {
		this.organizations = organizations;
	}

	public DashboardSummary() {

    }
}
