package com.barath.app.controller;

import com.barath.app.cloudfoundry.service.CloudFoundryService;
import com.barath.app.cloudfoundry.service.DashboardService;
import com.barath.app.utils.JacksonUtils;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceResponse;
import org.cloudfoundry.operations.services.ServiceInstance;
import org.cloudfoundry.operations.services.ServiceInstanceSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping(value="/cups", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserDefinedServiceInstancesController {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final CloudFoundryService cfService;
	private final DashboardService dashboardService;

	public UserDefinedServiceInstancesController(CloudFoundryService cfService,DashboardService dashboardService) {
		super();
		this.cfService = cfService;
		this.dashboardService= dashboardService;
	}
	
	
	@GetMapping("/{datacenter}/{organization}/{space}/{name}")
	public Mono<GetUserProvidedServiceInstanceResponse> getServiceInstanceByName(@PathVariable("datacenter") String datacenter,
																				 @PathVariable("space") String space,
																				 @PathVariable("organization") String organization,
																				 @PathVariable("name") String name) {
		
		if(logger.isInfoEnabled()) {
			logger.info("get service instance with name={} datacenter={}",name,datacenter);
		}
		return this.cfService.getUserProvidedServiceInstance(datacenter,organization,space,name);
		
	}
	
	@GetMapping("/{datacenter}/{organization}/{space}")
	public Flux<ServiceInstanceSummary> getServiceInstances(@PathVariable("datacenter") String datacenter,
															@PathVariable("space") String space,
															@PathVariable("organization") String organization) {
		
		if(logger.isInfoEnabled()) {
			logger.info("get service instances datacenter={} org={} space={}",datacenter,organization,space);
		}
		return this.cfService.getServices(datacenter,organization,space);
		
	}

	@GetMapping("/{datacenter}")
	public Map<String,Map<String,List<ServiceInstanceSummary>>> groupServicesByOrgAndSpace(@PathVariable("datacenter") String datacenter) {

		if(logger.isInfoEnabled()) {
			logger.info("get service instances datacenter={} ",datacenter);
		}
		return this.dashboardService.groupServicesByOrgAndSpace(datacenter);

	}

	@PutMapping("/{datacenter}/{organization}/{space}/{name}")
	public Mono<Void> updateServiceInstanceByName(@PathVariable("datacenter") String datacenter,
															 @PathVariable("space") String space,
															 @PathVariable("organization") String organization,
															 @PathVariable("name") String name, @RequestBody Map<String,String> credentials) {

		if(logger.isInfoEnabled()) {
			logger.info("update service instance with name={} datacenter={}",name,datacenter);
			logger.info("credentials to be updated {}", JacksonUtils.toJson(credentials));
		}
		return this.cfService.updateUserDefinedServiceInstanceByName(datacenter,organization,space,name,credentials);

	}

	@PostMapping("/{datacenter}/{organization}/{space}/{name}")
	public Mono<Void> createServiceInstance(@PathVariable("datacenter") String datacenter,
												  @PathVariable("space") String space,
												  @PathVariable("organization") String organization,
												  @PathVariable("name") String name, @RequestBody Map<String,String> credentials) {

		if(logger.isInfoEnabled()) {
			logger.info("update service instance with name={} datacenter={}",name,datacenter);
			logger.info("credentials to be updated {}", JacksonUtils.toJson(credentials));
		}
		return this.cfService.createUserDefinedServiceInstance(datacenter,organization,space,name,credentials);

	}
	

}
