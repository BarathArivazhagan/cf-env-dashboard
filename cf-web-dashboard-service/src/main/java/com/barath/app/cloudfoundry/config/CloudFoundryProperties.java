package com.barath.app.cloudfoundry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.barath.app.model.Organization;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(ignoreUnknownFields=true,prefix="cf")
public class CloudFoundryProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String,String> endpoints;

    private Map<String, List<Organization>> organizations;
 
    private String username;

    private String password;


    public Map<String, String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Map<String, String> endpoints) {
        this.endpoints = endpoints;
    }
    
    

   
    public Map<String, List<Organization>> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Map<String, List<Organization>> organizations) {
		this.organizations = organizations;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PostConstruct
    public void init() {
    	
      Assert.notNull(this.getEndpoints(), "endpoints targeted cannot be empty");
      Assert.notNull(this.getOrganizations(), "orgs targeted cannot be empty");
      
    }
    
   
}


