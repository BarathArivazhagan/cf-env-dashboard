package com.barath.app;

import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import com.barath.app.cloudfoundry.factory.CloudFoundryContext;
import com.barath.app.cloudfoundry.service.CloudFoundryService;
import com.barath.app.model.Organization;

import org.cloudfoundry.operations.applications.ApplicationSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AppsServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private CloudFoundryContext cloudFoundryContext;

    @Autowired
    private CloudFoundryProperties cloudFoundryProperties;

    @Autowired
    private CloudFoundryService cloudFoundryService;



    @Test
    public void testAppsByDataCenter(){

        String datacenter = "D2";
        Flux<ApplicationSummary> apps = Flux.empty();
        if( logger.isInfoEnabled()) { logger.info("get apps by datacenter {}",datacenter); }
        List<Organization> organizations = this.cloudFoundryProperties.getOrganizations().get(datacenter);

       Flux<String> spaces = Flux.fromIterable(organizations)
            .flatMap( org -> {
                System.out.println("ORG "+org);
                return Flux.fromIterable( this.cloudFoundryProperties.getOrganizations().get(org))
                        .flatMap( space -> {
                            return Flux.fromIterable(space.getSpaces());
                        });

            });

        spaces.doOnNext(o -> {
            System.out.println("O"+o);
        }).subscribe();


    }

}
