package com.barath.app.cloudfoundry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(ignoreUnknownFields=true,prefix="cf")
public class CloudFoundryProperties implements Serializable {

    private Map<String,String> endpoints;

    private Map<String, List<String>> organizations;

    private Map<String,List<String>> spaces;

    private String username;

    private String password;


    public Map<String, String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Map<String, String> endpoints) {
        this.endpoints = endpoints;
    }

    public Map<String, List<String>> getSpaces() {
        return spaces;
    }

    public void setSpaces(Map<String, List<String>> spaces) {
        this.spaces = spaces;
    }



    public Map<String, List<String>> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Map<String, List<String>> organizations) {
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

        endpoints.entrySet()
                .stream().forEach( entry -> {
            System.out.println("KEY "+entry.getKey()+" VALUE"+entry.getValue());
        });
        spaces.entrySet()
                .stream().forEach( entry -> {
            System.out.println("KEY "+entry.getKey()+" VALUE"+entry.getValue());
        });

        organizations.entrySet()
                .stream().forEach( entry -> {
            System.out.println("KEY "+entry.getKey()+" VALUE"+entry.getValue());
        });
    }

}
