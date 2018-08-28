package com.barath.app;


import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import com.barath.app.cloudfoundry.factory.CloudFoundryContext;
import com.barath.app.cloudfoundry.service.CloudFoundryService;
import com.barath.app.model.Organization;
import com.barath.app.utils.JacksonUtils;
import io.jsonwebtoken.lang.Collections;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.applications.ApplicationSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DashboardSummaryTest {

    @Autowired
    private CloudFoundryContext cloudFoundryContext;

    @Autowired
    private CloudFoundryProperties cloudFoundryProperties;

    @Autowired
    private CloudFoundryService cloudFoundryService;


    @Test
    public void testGroupAppsByDatacenter(){

        Map<String,List<ApplicationSummary>> applications= new HashMap();
        this.cloudFoundryProperties.getEndpoints()
                .entrySet().parallelStream()
                .forEach( entry -> {

                    String datacenter =entry.getKey();

                    System.out.println("KEY "+datacenter+" VALUE "+entry.getValue());
                    List<Organization> organizations = this.cloudFoundryProperties.getOrganizations().get(entry.getKey());
                    if(!Collections.isEmpty(organizations)) {
                        organizations.stream()
                                .forEach(org -> {

                                    List<String> spaces = this.cloudFoundryProperties.getOrganizations().get(datacenter).stream()
                                    						.filter( a -> a.getName().equals(datacenter))
                                    							.findFirst().get().getSpaces();
                                    						
                                    								
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

        System.out.println(JacksonUtils.toJson(applications));


    }



}
