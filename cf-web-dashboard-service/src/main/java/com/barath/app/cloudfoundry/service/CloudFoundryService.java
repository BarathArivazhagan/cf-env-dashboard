package com.barath.app.cloudfoundry.service;

import java.util.Map;

import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.UpdateUserProvidedServiceInstanceResponse;
import org.cloudfoundry.operations.applications.ApplicationSummary;
import org.cloudfoundry.operations.services.ServiceInstance;
import org.cloudfoundry.operations.services.ServiceInstanceSummary;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CloudFoundryService {							
	
	Flux<ApplicationSummary> getApps(String datacenter, String org, String space);
	Flux<ApplicationSummary> getAppsByDatacenter(String datacenter);
	Mono<Void> startApplication(String datacenter, String org, String space, String appName);
	Mono<Void> stopApplication(String datacenter, String org, String space, String appName);
	Mono<Void> restartApplication(String datacenter, String org, String space, String appName);
	Mono<Void> restageApplication(String datacenter, String org, String space, String appName);
	Flux<ServiceInstanceSummary> getServices(String datacenter, String org, String space);
	Mono<ServiceInstance> getServiceInstanceByName(String datacenter, String org, String space, String name);
	Mono<GetUserProvidedServiceInstanceResponse> getUserProvidedServiceInstance(String datacenter, String org, String space, String name);
	Mono<UpdateUserProvidedServiceInstanceResponse> updateUserDefinedServiceInstanceByName(String datacenter, String org, String space, String name, Map<String,String> credentials);
	Mono<Void> createUserDefinedServiceInstance(String datacenter, String org, String space, String name, Map<String,String> credentials);
}
