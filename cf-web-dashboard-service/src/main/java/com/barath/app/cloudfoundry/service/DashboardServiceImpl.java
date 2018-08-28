package com.barath.app.cloudfoundry.service;

import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import com.barath.app.cloudfoundry.factory.CloudFoundryContext;
import com.barath.app.model.DashboardSummary;
import com.barath.app.model.Organization;
import com.barath.app.utils.JacksonUtils;
import io.jsonwebtoken.lang.Collections;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.applications.ApplicationSummary;
import org.cloudfoundry.operations.services.ServiceInstanceSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CloudFoundryService cloudFoundryService;

    private final CloudFoundryProperties cloudFoundryProperties;

    public DashboardServiceImpl(CloudFoundryService cloudFoundryService, CloudFoundryProperties cloudFoundryProperties) {
        this.cloudFoundryService = cloudFoundryService;
        this.cloudFoundryProperties = cloudFoundryProperties;
    }

    public DashboardSummary getDashboardSummary(){

       DashboardSummary dashboardSummary = new DashboardSummary();
       dashboardSummary.setApplications(getApplications());
       dashboardSummary.setDatacenters(this.cloudFoundryProperties.getEndpoints());
       dashboardSummary.setOrganizations(this.cloudFoundryProperties.getOrganizations());
       dashboardSummary.setServices(this.getServices());

       if(logger.isInfoEnabled()){
           logger.info("summary found {}", JacksonUtils.toJson(dashboardSummary));
       }
       return dashboardSummary;
    }

    @Override
    public  List<ApplicationSummary> getApplicationsByDatacenter(String datacenter){

        return this.getApplications().get(datacenter);
    }

    @Override
    public Map<String, List<ApplicationSummary>> getApplications(){

        Map<String,List<ApplicationSummary>> applications= new HashMap();
        this.cloudFoundryProperties.getEndpoints()
                .entrySet().parallelStream()
                .forEach( entry -> {

                    String datacenter =entry.getKey();

                    if(logger.isInfoEnabled()) {logger.info("KEY {} VALUE={}",datacenter,entry.getValue()); }
                    List<Organization> organizations = this.cloudFoundryProperties.getOrganizations().get(entry.getKey());
                    if(!Collections.isEmpty(organizations)) {
                        organizations.stream()
                                .forEach(org -> {

                                    List<String> spaces = org.getSpaces();
                                    if( !Collections.isEmpty(spaces)) {
                                        spaces.stream().forEach(space -> {
                                            Flux<ApplicationSummary> applicationSummaryFlux = this.cloudFoundryService.getApps(datacenter, org.getName(), space);
                                            List<ApplicationSummary> apps = applicationSummaryFlux
                                                    .collectList().block();

                                            applications.put(datacenter, apps);

                                        });
                                    }


                                });
                    }

                });

        return applications;
    }



    @Override
    public  List<ServiceInstanceSummary> getServicesByDatacenter(String datacenter){

        return this.getServices().get(datacenter);
    }


    @Override
    public Map<String, List<ServiceInstanceSummary>> getServices(){

        Map<String,List<ServiceInstanceSummary>> services= new HashMap();
        this.cloudFoundryProperties.getEndpoints()
                .entrySet().parallelStream()
                .forEach( entry -> {

                    String datacenter =entry.getKey();

                    if(logger.isInfoEnabled()) {logger.info("KEY {} VALUE={}",datacenter,entry.getValue()); }
                    List<Organization> organizations = this.cloudFoundryProperties.getOrganizations().get(entry.getKey());
                    if(!Collections.isEmpty(organizations)) {
                        organizations.stream()
                                .forEach(org -> {

                                    List<String> spaces =org.getSpaces();
                                    if( !Collections.isEmpty(spaces)) {
                                        spaces.stream().forEach(space -> {
                                            Flux<ServiceInstanceSummary> serviceInstanceSummaryFlux = this.cloudFoundryService.getServices(datacenter, org.getName(), space);
                                            List<ServiceInstanceSummary> svcs = serviceInstanceSummaryFlux
                                                    .collectList().block();

                                            services.put(datacenter, svcs);

                                        });
                                    }


                                });
                    }

                });

        return services;
    }

    @Override
    public Map<String,Map<String,List<ApplicationSummary>>> groupAppsByOrgAndSpace(String datacenter){

        if(logger.isInfoEnabled()){
            logger.info("grouping apps by org and space");
        }
        List<Organization> orgs = this.cloudFoundryProperties.getOrganizations().get(datacenter);
        Map<String,Map<String,List<ApplicationSummary>>> orgApps = new HashMap<>();
        orgs.forEach( org -> {

            List<String> spaces =org.getSpaces();
            spaces.forEach( space ->{
                Map<String,List<ApplicationSummary>> spaceApps = new HashMap<>();
                List<ApplicationSummary> applicationSummaries = this.cloudFoundryService.getApps(datacenter,org.getName(),space).collectList().block();
                spaceApps.put(space,applicationSummaries);
                orgApps.put(org.getName(),spaceApps);
            });

        });
        return  orgApps;


    }

    @Override
    public Map<String,Map<String,List<ServiceInstanceSummary>>> groupServicesByOrgAndSpace(String datacenter){

        if(logger.isInfoEnabled()){
            logger.info("grouping apps by org and space");
        }
        List<Organization> orgs = this.cloudFoundryProperties.getOrganizations().get(datacenter);
        Map<String,Map<String,List<ServiceInstanceSummary>>> orgServices = new HashMap<>();
        orgs.forEach( org -> {

            List<String> spaces = org.getSpaces();
            spaces.forEach( space ->{
                Map<String,List<ServiceInstanceSummary>> spaceServices = new HashMap<>();
                List<ServiceInstanceSummary> serviceInstanceSummaries = this.cloudFoundryService.getServices(datacenter,org.getName(),space).collectList().block();
                spaceServices.put(space,serviceInstanceSummaries);
                orgServices.put(org.getName(),spaceServices);
            });

        });
        return  orgServices;


    }


}
