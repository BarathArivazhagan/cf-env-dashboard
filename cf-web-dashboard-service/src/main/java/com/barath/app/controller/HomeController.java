package com.barath.app.controller;


import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/home")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CloudFoundryProperties cloudFoundryProperties;

    public HomeController(CloudFoundryProperties cloudFoundryProperties) {
        this.cloudFoundryProperties = cloudFoundryProperties;
    }

    @GetMapping
    public String index(){
        return "WELCOME TO CF CLIENT HELPER";
    }


    @GetMapping(value = "/datacenters")
    public List<String> getDatacenters(){
        return this.cloudFoundryProperties.getEndpoints().entrySet()
                .stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @GetMapping(value = "/orgs/{datacenter}")
    public List<String> getOrgsByDatacenter(@PathVariable String datacenter){
        return this.cloudFoundryProperties.getOrganizations().get(datacenter);
    }

    @GetMapping(value = "/spaces/{datacenter}")
    public List<String> getSpaces(@PathVariable String datacenter){
        List<String> orgs =this.cloudFoundryProperties.getOrganizations().get(datacenter);

        return orgs.stream().flatMap( org1 -> {
        return this.cloudFoundryProperties.getSpaces().get(org1).stream();
        }).collect(Collectors.toList());
    }

}
