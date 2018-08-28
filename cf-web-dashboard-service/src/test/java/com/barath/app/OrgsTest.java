package com.barath.app;

import java.lang.invoke.MethodHandles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.barath.app.cloudfoundry.config.CloudFoundryProperties;
import com.barath.app.cloudfoundry.factory.CloudFoundryContext;
import com.barath.app.cloudfoundry.service.CloudFoundryService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrgsTest {
	
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private CloudFoundryContext cloudFoundryContext;

    @Autowired
    private CloudFoundryProperties cloudFoundryProperties;

    @Autowired
    private CloudFoundryService cloudFoundryService;
    
    
    @Test
    public void testOrgsByDatacenter() {
    	String org = "";
    		
    }

}
