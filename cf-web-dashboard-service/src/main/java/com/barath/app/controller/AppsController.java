package com.barath.app.controller;

import com.barath.app.cloudfoundry.service.CloudFoundryService;

import com.barath.app.cloudfoundry.service.DashboardService;
import org.cloudfoundry.operations.applications.ApplicationSummary;
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
@RequestMapping(value="/apps", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AppsController {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final CloudFoundryService cfService;
	private final DashboardService dashboardService;
	
	
	public AppsController(CloudFoundryService cfService, DashboardService dashboardService) {
		super();
		this.cfService = cfService;
		this.dashboardService = dashboardService;
	}



	@GetMapping("/{datacenter}/{organization}/{space}")
	public Flux<ApplicationSummary> findApps(@PathVariable("datacenter") String datacenter,
											 @PathVariable("space") String space,
											 @PathVariable("organization") String organization){

		if( logger.isInfoEnabled()) {
			logger.info("getting apps controller start datacenter={} org={} space={} ",datacenter,organization, space);
		}
		return this.cfService.getApps(datacenter, organization, space );

	}

	@GetMapping("/{datacenter}")
	public Map<String, Map<String,List<ApplicationSummary>>> groupAppsByDatacenter(@PathVariable("datacenter") String datacenter){

		if( logger.isInfoEnabled()) {
			logger.info("getting apps controller start datacenter={} ",datacenter);
		}
		return this.dashboardService.groupAppsByOrgAndSpace(datacenter);

	}


	@PostMapping("/{datacenter}/{organization}/{space}/{appName}")
	public Mono<Void> startAppByName(@PathVariable("datacenter") String datacenter,
									 @PathVariable("organization") String organization,
									 @PathVariable("space") String space,
									 @PathVariable("appName") String appName){

		if( logger.isInfoEnabled()) {
			logger.info("start application datacenter={} org={} space={} ",datacenter,organization, space);
		}
		return this.cfService.startApplication(datacenter, organization, space, appName );

	}

	@DeleteMapping("/{datacenter}/{organization}/{space}/{appName}")
	public Mono<Void> stopByAppName(@PathVariable("datacenter") String datacenter,
												  @PathVariable("organization") String organization,
												  @PathVariable("space") String space,
												  @PathVariable("appName") String appName){

		if( logger.isInfoEnabled()) {
			logger.info("getting apps controller start datacenter={} org={} space={} appName= {}",datacenter,organization, space,appName);
		}
		return this.cfService.stopApplication(datacenter, organization, space , appName );

	}

	@GetMapping("/{datacenter}/{organization}/{space}/{appName}")
	public Mono<Void> restageByAppName(@PathVariable("datacenter") String datacenter,
											   @PathVariable("organization") String organization,
											   @PathVariable("space") String space,
											   @PathVariable("appName") String appName){

		if( logger.isInfoEnabled()) {
			logger.info("getting apps controller start datacenter={} org={} space={} appName= {}",datacenter,organization, space,appName);
		}
		return this.cfService.restageApplication(datacenter, organization, space, appName);

	}

	@PutMapping("/{datacenter}/{organization}/{space}/{appName}")
	public Mono<Void> restartByAppName(@PathVariable("datacenter") String datacenter,
											   @PathVariable("organization") String organization,
											   @PathVariable("space") String space,
											   @PathVariable("appName") String appName){

		if( logger.isInfoEnabled()) {
			logger.info("getting apps controller start datacenter={} org={} space={} appName={} ",datacenter,organization, space,appName);
		}
		return this.cfService.restartApplication(datacenter, organization, space, appName );

	}
}
