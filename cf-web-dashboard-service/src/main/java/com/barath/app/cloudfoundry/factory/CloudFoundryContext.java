package com.barath.app.cloudfoundry.factory;

import com.barath.app.cloudfoundry.constants.CF_CONSTANTS;
import io.jsonwebtoken.lang.Assert;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.uaa.UaaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class CloudFoundryContext implements CloudFoundryFactory {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ApplicationContext applicationContext;

    public CloudFoundryContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public ConnectionContext getConnectionContext(String datacenter){

        Assert.notNull(datacenter,"datacenter cannot be empty");
        if(logger.isInfoEnabled()) { logger.info("get connection context with name {}",datacenter); }
        String beanName = datacenter.concat(CF_CONSTANTS.CONNECTION_CONTEXT_BEAN_SUFFIX);
        if(logger.isInfoEnabled()) { logger.info("lookup bean name {}",beanName); }
        ConnectionContext connectionContext = (ConnectionContext) this.applicationContext.getBean(beanName);
        if(connectionContext !=null && logger.isInfoEnabled()) { logger.info("cloud foundry client bean found {} for datacenter {}",connectionContext,datacenter); }
        return connectionContext;
    }

    @Override
    public TokenProvider getTokenProvider(String datacenter){

        Assert.notNull(datacenter,"datacenter cannot be empty");
        if(logger.isInfoEnabled()) { logger.info("get token provider with name {}",datacenter); }
        String beanName = datacenter.concat(CF_CONSTANTS.TOKEN_PROVIDER_BEAN_SUFFIX);
        if(logger.isInfoEnabled()) { logger.info("lookup bean name {}",beanName); }
        TokenProvider tokenProvider = (TokenProvider) this.applicationContext.getBean(beanName);
        if(tokenProvider !=null && logger.isInfoEnabled()) { logger.info("token provider bean found {} for datacenter {}",tokenProvider,datacenter); }
        return tokenProvider;
    }

    @Override
    public CloudFoundryClient getCloudFoundryClient(String datacenter) {

        Assert.notNull(datacenter,"datacenter cannot be empty");
        if(logger.isInfoEnabled()) { logger.info("get cloud foundry client with name {}",datacenter); }
        String beanName = datacenter.concat(CF_CONSTANTS.CLOUD_FOUNDRY_CLIENT_BEAN_SUFFIX);
        if(logger.isInfoEnabled()) { logger.info("lookup bean name {}",beanName); }
        CloudFoundryClient cloudFoundryClient = (CloudFoundryClient) this.applicationContext.getBean(beanName);
        if(cloudFoundryClient !=null && logger.isInfoEnabled()) { logger.info("cloud foundry client bean found {} for datacenter {}",cloudFoundryClient,datacenter); }
        return cloudFoundryClient;
    }

    @Override
    public DopplerClient getDopplerClient(String datacenter) {
        Assert.notNull(datacenter,"datacenter cannot be empty");
        if(logger.isInfoEnabled()) { logger.info("get cloud foundry client with name {}",datacenter); }
        String beanName = datacenter.concat(CF_CONSTANTS.DOPPLER_CLIENT_BEAN_SUFFIX);
        if(logger.isInfoEnabled()) { logger.info("lookup bean name {}",beanName); }
        DopplerClient dopplerClient = (DopplerClient) this.applicationContext.getBean(beanName);
        if(dopplerClient !=null && logger.isInfoEnabled()) { logger.info("doppler client bean found {} for datacenter {}",dopplerClient,datacenter); }
        return dopplerClient;

    }

    @Override
    public UaaClient getUaaClient(String datacenter) {
         Assert.notNull(datacenter,"datacenter cannot be empty");
         if(logger.isInfoEnabled()) { logger.info("get uaa client with name {}",datacenter); }
         String beanName = datacenter.concat(CF_CONSTANTS.UAA_CLIENT_BEAN_SUFFIX);
         if(logger.isInfoEnabled()) { logger.info("lookup bean name {}",beanName); }
         UaaClient uaaClient = (UaaClient) this.applicationContext.getBean(beanName);
         if(uaaClient !=null && logger.isInfoEnabled()) { logger.info(" uaa client bean found {} for datacenter {}",uaaClient,datacenter); }
         return uaaClient;
    }

    @Override
    public  CloudFoundryOperations getCloudFoundryOperations(String datacenter, String org, String space){

        Assert.notNull(datacenter,"datacenter cannot be empty");
        Assert.notNull(org,"org cannot be empty");
        Assert.notNull(space,"space cannot be empty");
        if(logger.isInfoEnabled()) { logger.info("get cloud foundry operations with name {}",datacenter); }
        String beanName = datacenter.concat(org).concat(space).concat(CF_CONSTANTS.CLOUD_FOUNDRY_OPERATIONS_BEAN_SUFFIX);
        if(logger.isInfoEnabled()) { logger.info("lookup bean name {}",beanName); }
        CloudFoundryOperations cloudFoundryOperations = (CloudFoundryOperations) this.applicationContext.getBean(beanName);
        if(cloudFoundryOperations !=null && logger.isInfoEnabled()) { logger.info("  cloud foundry operations bean found {} for datacenter {} with space {} org {}",cloudFoundryOperations,datacenter,space,org); }
        return cloudFoundryOperations;
    }
}
