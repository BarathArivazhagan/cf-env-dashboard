package com.barath.app.cloudfoundry.service;

import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import com.barath.app.cloudfoundry.factory.CloudFoundryContext;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.applications.*;
import org.cloudfoundry.operations.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CloudFoundryService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CloudFoundryContext cloudFoundryContext;
    private final CloudFoundryProperties cloudFoundryProperties;

    public CloudFoundryService(CloudFoundryContext cloudFoundryContext,CloudFoundryProperties cloudFoundryProperties) {
        this.cloudFoundryContext = cloudFoundryContext;
        this.cloudFoundryProperties= cloudFoundryProperties;

    }

    public Flux<ApplicationSummary> getApps(String datacenter, String org, String space){

        CloudFoundryOperations cfOperations = this.cloudFoundryContext.getCloudFoundryOperations(datacenter,org,space);
        return cfOperations.applications().list();
    }

    public Flux<ApplicationSummary> getAppsByDatacenter(String datacenter){

        Flux<ApplicationSummary> applicationSummaryFlux = Flux.empty();
        List<ApplicationSummary> apps = new ArrayList<>(3);
        if( logger.isInfoEnabled()) { logger.info("get apps by datacenter {}",datacenter); }
        List<String> organizations = this.cloudFoundryProperties.getOrganizations().get(datacenter);
        if(organizations.isEmpty()) {
            organizations.forEach( org -> {

                List<String> spaces = this.cloudFoundryProperties.getSpaces().get(org);
                spaces.forEach( space ->{
                    applicationSummaryFlux.concatWith(this.getApps(datacenter,org,space));
                });
            });
        }

        return applicationSummaryFlux;
    }

    public Mono<Void> startApplication(String datacenter, String org, String space, String appName){
        CloudFoundryOperations cfOperations = this.cloudFoundryContext.getCloudFoundryOperations(datacenter,org,space);
        StartApplicationRequest request = StartApplicationRequest.builder()
                                            .name(appName)
                                            .build();
         return cfOperations.applications().start(request);

    }

    public Mono<Void> stopApplication(String datacenter, String org, String space, String appName){
        CloudFoundryOperations cfOperations = this.cloudFoundryContext.getCloudFoundryOperations(datacenter,org,space);
        StopApplicationRequest request = StopApplicationRequest.builder()
                .name(appName)
                .build();
        return cfOperations.applications().stop(request);
    }

    public Mono<Void> restartApplication(String datacenter, String org, String space, String appName){
        CloudFoundryOperations cfOperations = this.cloudFoundryContext.getCloudFoundryOperations(datacenter,org,space);
        RestartApplicationRequest request = RestartApplicationRequest.builder()
                .name(appName)
                .build();
        return cfOperations.applications().restart(request);
    }

    public Mono<Void> restageApplication(String datacenter, String org, String space, String appName){
        CloudFoundryOperations cfOperations = this.cloudFoundryContext.getCloudFoundryOperations(datacenter,org,space);
        RestageApplicationRequest request = RestageApplicationRequest.builder()
                .name(appName)
                .build();
        return cfOperations.applications().restage(request);
    }

    public Flux<ServiceInstanceSummary> getServices(String datacenter, String org, String space){

        CloudFoundryOperations cfOperations = this.cloudFoundryContext.getCloudFoundryOperations(datacenter,org,space);
        return cfOperations.services().listInstances();
    }

    public Mono<ServiceInstance> getServiceInstanceByName(String datacenter, String org, String space, String name){
        CloudFoundryOperations cfOperations = this.cloudFoundryContext.getCloudFoundryOperations(datacenter,org,space);
        GetServiceInstanceRequest request = GetServiceInstanceRequest.builder()
                                                    .name(name).build();
        return cfOperations.services().getInstance(request);
    }

    public Mono<Void> createUserDefinedServiceInstance(String datacenter, String org, String space, String name, Map<String,String> credentials){

        CloudFoundryOperations cfOperations = this.cloudFoundryContext.getCloudFoundryOperations(datacenter,org,space);
        CreateUserProvidedServiceInstanceRequest request = CreateUserProvidedServiceInstanceRequest.builder()
                .putAllCredentials(credentials)
                .name(name)
                .build();
        return cfOperations.services().createUserProvidedInstance(request);
    }

    public Mono<Void> updateUserDefinedServiceInstanceByName(String datacenter, String org, String space, String name, Map<String,String> credentials){

        CloudFoundryOperations cfOperations = this.cloudFoundryContext.getCloudFoundryOperations(datacenter,org,space);
        Mono<ServiceInstance> serviceInstanceMono = this.getServiceInstanceByName(datacenter,org,space,name);
        ServiceInstance serviceInstance = serviceInstanceMono.block();
        UpdateUserProvidedServiceInstanceRequest request = UpdateUserProvidedServiceInstanceRequest.builder()
                .putAllCredentials(credentials)
                .userProvidedServiceInstanceName(name)
                .build();
        return cfOperations.services().updateUserProvidedInstance(request);
    }
}
