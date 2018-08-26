package com.barath.app.cloudfoundry.builder;

import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import com.barath.app.cloudfoundry.constants.CF_CONSTANTS;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Component
public class CloudFoundryBeanProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ConfigurableApplicationContext applicationContext;
    private final CloudFoundryProperties cloudFoundryProperties;
    private final ConfigurableBeanFactory beanFactory;

    public CloudFoundryBeanProcessor(ConfigurableApplicationContext applicationContext
        ,CloudFoundryProperties cloudFoundryProperties) {
        this.applicationContext = applicationContext;
        this.cloudFoundryProperties = cloudFoundryProperties;
        this.beanFactory = applicationContext.getBeanFactory();
    }

    @PostConstruct
    public void process(){

        cloudFoundryProperties.getEndpoints().forEach( (datacenter, apiHost) -> {

            if(logger.isInfoEnabled()){
                logger.info("datacenter {} apihost {}",datacenter,apiHost);
            }
            ConnectionContext connectionContext = connectionContext(apiHost);
            TokenProvider tokenProvider = tokenProvider(cloudFoundryProperties.getUsername(),cloudFoundryProperties.getPassword());
            if(logger.isInfoEnabled()){
                logger.info("connection context {} token provider {} for datacenter {} ",connectionContext, tokenProvider,datacenter);
            }
            this.beanFactory.registerSingleton(getConnectionContextBeanName(datacenter),connectionContext);
            this.beanFactory.registerSingleton(getTokenProviderBeanName(datacenter),tokenProvider);
            this.beanFactory.registerSingleton(getCloudFoundryClientBeanName(datacenter), cloudFoundryClient(connectionContext,tokenProvider));
            this.beanFactory.registerSingleton(getUaaClientBeanName(datacenter), uaaClient(connectionContext,tokenProvider));
            this.beanFactory.registerSingleton(getDopplerClientBeanName(datacenter), dopplerClient(connectionContext,tokenProvider));
        });

        // to debug the bean names present in the context
        if(logger.isDebugEnabled()){

            for(String beanName: this.applicationContext.getBeanDefinitionNames()){
                logger.debug("bean name {}",beanName);
            }

        }

    }

    private String getConnectionContextBeanName(String datacenter){

        if(!StringUtils.isEmpty(datacenter)){
            return datacenter.concat(CF_CONSTANTS.CONNECTION_CONTEXT_BEAN_SUFFIX);
        }else{
            return CF_CONSTANTS.DEFAULT_CONNECTION_CONTEXT_BEAN_NAME;
        }

    }

    private String getTokenProviderBeanName(String datacenter){

        if(!StringUtils.isEmpty(datacenter)){
            return datacenter.concat(CF_CONSTANTS.TOKEN_PROVIDER_BEAN_SUFFIX);
        }else{
            return CF_CONSTANTS.DEFAULT_TOKEN_PROVIDER_BEAN_NAME;
        }

    }

    private String getDopplerClientBeanName(String datacenter){

        if(!StringUtils.isEmpty(datacenter)){
            return datacenter.concat(CF_CONSTANTS.DOPPLER_CLIENT_BEAN_SUFFIX);
        }else{
            return CF_CONSTANTS.DEFAULT_DOPPLER_CLIENT_BEAN_NAME;
        }

    }

    private String getCloudFoundryClientBeanName(String datacenter){

        if(!StringUtils.isEmpty(datacenter)){
            return datacenter.concat(CF_CONSTANTS.CLOUD_FOUNDRY_CLIENT_BEAN_SUFFIX);
        }else{
            return CF_CONSTANTS.DEFAULT_CLOUD_FOUNDRY_CLIENT_BEAN_NAME;
        }

    }

    private String getUaaClientBeanName(String datacenter){

        if(!StringUtils.isEmpty(datacenter)){
            return datacenter.concat(CF_CONSTANTS.UAA_CLIENT_BEAN_SUFFIX);
        }else{
            return CF_CONSTANTS.DEFAULT_UAA_CLIENT_BEAN_NAME;
        }

    }



    protected DefaultConnectionContext connectionContext(String apiHost) {
        return DefaultConnectionContext.builder()
                .apiHost(apiHost)
                .build();
    }


    protected PasswordGrantTokenProvider tokenProvider( String username, String password) {
        return PasswordGrantTokenProvider.builder()
                .password(password)
                .username(username)
                .build();
    }


    protected ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }


    protected ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorDopplerClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }


    protected ReactorUaaClient uaaClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorUaaClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }
}
