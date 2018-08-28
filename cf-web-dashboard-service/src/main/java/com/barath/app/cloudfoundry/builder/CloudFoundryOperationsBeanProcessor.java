package com.barath.app.cloudfoundry.builder;

import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import com.barath.app.cloudfoundry.constants.CF_CONSTANTS;


import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.uaa.UaaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.List;


@AutoConfigureAfter(CloudFoundryBeanProcessor.class)
@Configuration
public class CloudFoundryOperationsBeanProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ConfigurableApplicationContext applicationContext;
    private final CloudFoundryProperties cloudFoundryProperties;
    private final ConfigurableBeanFactory beanFactory;


    public CloudFoundryOperationsBeanProcessor(ConfigurableApplicationContext applicationContext, CloudFoundryProperties cloudFoundryProperties, ConfigurableBeanFactory beanFactory) {
        this.applicationContext = applicationContext;
        this.cloudFoundryProperties = cloudFoundryProperties;
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void process(){
    	
    	this.populateCloudFoundryOperationBeans();
       
    }


	protected void populateCloudFoundryOperationBeans() {
		
		this.cloudFoundryProperties.getOrganizations()
         .forEach( (datacenter , organizations) -> {

             if(logger.isInfoEnabled()) { logger.info("orgs {} datacenter {}",organizations,datacenter); }
             organizations.forEach( organization -> {
             		
            	 String org = organization.getName();
                 List<String> spaces = organization.getSpaces();
                 
                 if(logger.isInfoEnabled()){
                     logger.info("spaces {} for org {}",spaces,org);
                 }
                 spaces.forEach( space -> {

                     DefaultCloudFoundryOperations defaultCloudFoundryOperations =cloudFoundryOperations(datacenter,org,space);
                     if( defaultCloudFoundryOperations !=null && logger.isInfoEnabled()){
                         logger.info("defaultCloudFoundryOperations {}",defaultCloudFoundryOperations);
                     }else{
                         logger.error("defaultCloudFoundryOperations not created");
                     }
                     this.beanFactory.registerSingleton(getCloudFoundryOperationsBeanName(datacenter,org,space),
                             defaultCloudFoundryOperations);
                 });

             });
            
         });
		
	}
    
    
    
    private String getCloudFoundryOperationsBeanName(String datacenter,String org, String space){

        return datacenter.concat(org).concat(space).concat(CF_CONSTANTS.CLOUD_FOUNDRY_OPERATIONS_BEAN_SUFFIX);
    }
    

    protected DefaultCloudFoundryOperations cloudFoundryOperations(String datacenter,String org, String space){

        CloudFoundryClient cloudFoundryClient = (CloudFoundryClient) this.applicationContext.getBean(datacenter.concat(CF_CONSTANTS.CLOUD_FOUNDRY_CLIENT_BEAN_SUFFIX));
        DopplerClient dopplerClient = (DopplerClient) this.applicationContext.getBean(datacenter.concat(CF_CONSTANTS.DOPPLER_CLIENT_BEAN_SUFFIX));
        UaaClient uaaClient = (UaaClient) this.applicationContext.getBean(datacenter.concat(CF_CONSTANTS.UAA_CLIENT_BEAN_SUFFIX));
        return  cloudFoundryOperations(cloudFoundryClient,dopplerClient,uaaClient,org,space);
    }
    
    
    protected DefaultCloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cloudFoundryClient,
                                                                   DopplerClient dopplerClient,
                                                                   UaaClient uaaClient,
                                                                   String organization,
                                                                   String space) {
        return DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(cloudFoundryClient)
                .dopplerClient(dopplerClient)
                .uaaClient(uaaClient)
                .organization(organization)
                .space(space)
                .build();
    }
    
    
    
    
    
}
