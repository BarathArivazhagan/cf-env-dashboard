package com.barath.app.controller;

import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import com.barath.app.cloudfoundry.service.DashboardService;
import com.barath.app.model.DashboardSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@CrossOrigin("*")
@RestController
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardSummary summary(){
        if(logger.isInfoEnabled()){
            logger.info("retrieving dashboard summary");
        }
        return  this.dashboardService.getDashboardSummary();
    }
}
