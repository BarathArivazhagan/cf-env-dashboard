package com.barath.app;

import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import com.barath.app.cloudfoundry.factory.CloudFoundryContext;
import com.barath.app.cloudfoundry.service.CloudFoundryService;
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




    //@Test
    public void testAppsByDataCenter(){

        String datacenter = "D1";
        List<String> orgs = this.cloudFoundryProperties.getOrganizations().get(datacenter);
        System.out.println(orgs);
        Map<String,List<String>> spaces = this.cloudFoundryProperties.getSpaces();
        System.out.println(spaces);
        Flux.fromIterable(orgs)
                .doOnNext( org ->{
                    System.out.println("ORG NAME"+org);

                }).subscribe();
        Flux<List<String>> spaceFlux = Flux.fromIterable(orgs)
                .flatMap( s -> {

                    List<String> spaces1 = this.cloudFoundryProperties.getSpaces().get(s);
                    return Flux.just(spaces1);
                });

        spaceFlux.doOnNext( ss -> {
            System.out.println("space name"+ss);
        }).subscribe();
    }

    @Test
    public void test(){

        String datacenter = "D2";
        Flux<ApplicationSummary> apps = Flux.empty();
        if( logger.isInfoEnabled()) { logger.info("get apps by datacenter {}",datacenter); }
        List<String> organizations = this.cloudFoundryProperties.getOrganizations().get(datacenter);

       Flux<String> spaces = Flux.fromIterable(organizations)
            .flatMap( org -> {
                System.out.println("ORG "+org);
                return Flux.fromIterable( this.cloudFoundryProperties.getSpaces().get(org))
                        .flatMap( space -> {
                            return Flux.just(space);
                        });

            });

        spaces.doOnNext(o -> {
            System.out.println("O"+o);
        }).subscribe();
//        System.out.println("space "+space);
//        return this.cloudFoundryService.getApps(datacenter,org,space);
//        if(organizations.isEmpty()) {
//            organizations.forEach( org -> {
//
//                List<String> spaces = this.cloudFoundryProperties.getSpaces().get(org);
//                spaces.forEach( space ->{
//                    apps.concatWith(this.cloudFoundryService.getApps(datacenter,org,space));
//                });
//            });
//        }
//        List<ApplicationSummary> summaries= new ArrayList<>();
//        apps.subscribe(summaries::add)  ;
//        summaries.stream()
//                .map(ApplicationSummary::getName).forEach(System.out::println);

    }

}
