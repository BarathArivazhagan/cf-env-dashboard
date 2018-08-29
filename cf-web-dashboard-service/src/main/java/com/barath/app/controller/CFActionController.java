package com.barath.app.controller;

import com.barath.app.cloudfoundry.service.CloudFoundryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/actions")
public class CFActionController {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private CloudFoundryService cloudFoundryService;

    public CFActionController(CloudFoundryService cloudFoundryService) {
        this.cloudFoundryService = cloudFoundryService;
    }

    @PostMapping("/start/{datacenter}/{org}/{space}/{appName}")
    public Mono<Void> startAction(@PathVariable String datacenter,
                                  @PathVariable String org,
                                  @PathVariable String space,
                                  @PathVariable String appName){
        if(logger.isInfoEnabled()){
            logger.info("start application with appname={} datacenter={} org={} space={}",appName,datacenter,org,space);
        }
        return this.cloudFoundryService.startApplication(datacenter, org, space, appName);
    }

    @PostMapping("/stop/{datacenter}/{org}/{space}/{appName}")
    public  Mono<Void> stopAction(@PathVariable String datacenter,
                            @PathVariable String org,
                            @PathVariable String space,
                            @PathVariable String appName){

        if(logger.isInfoEnabled()){
            logger.info("stop application with appname={} datacenter={} org={} space={}",appName,datacenter,org,space);
        }
        return this.cloudFoundryService.stopApplication(datacenter, org, space, appName);

    }

    @PostMapping("/restart/{datacenter}/{org}/{space}/{appName}")
    public Mono<Void> restartAction(@PathVariable String datacenter,
                                    @PathVariable String org,
                                    @PathVariable String space,
                                    @PathVariable String appName){

        if(logger.isInfoEnabled()){
            logger.info("restart application with appname={} datacenter={} org={} space={}",appName,datacenter,org,space);
        }
        return this.cloudFoundryService.restartApplication(datacenter, org, space, appName);

    }

    @PostMapping("/restage/{datacenter}/{org}/{space}/{appName}")
    public  Mono<Void> restageAction(@PathVariable String datacenter,
                            @PathVariable String org,
                            @PathVariable String space,
                            @PathVariable String appName){
        if(logger.isInfoEnabled()){
            logger.info("restage application with appname={} datacenter={} org={} space={}",appName,datacenter,org,space);
        }
        return this.cloudFoundryService.restageApplication(datacenter, org, space, appName);
    }

}
