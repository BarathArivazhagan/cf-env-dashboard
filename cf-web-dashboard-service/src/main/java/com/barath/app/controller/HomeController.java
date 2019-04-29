package com.barath.app.controller;


import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import com.barath.app.model.Organization;

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
        return this.cloudFoundryProperties.getOrganizations().get(datacenter).stream().map(Organization::getName).collect(Collectors.toList());
    }

    @GetMapping(value = "/spaces/{datacenter}")
    public List<String> getSpaces(@PathVariable String datacenter){
        List<Organization> orgs =this.cloudFoundryProperties.getOrganizations().get(datacenter);

        return orgs.stream().map(Organization::getSpaces).flatMap(List::stream).collect(Collectors.toList());
    }

}
