package com.barath.app;

import org.cloudfoundry.operations.CloudFoundryOperations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {

	@Autowired
	private ApplicationContext context;

	@Test
	public void testBeans() {

		Map<String,CloudFoundryOperations> beans=this.context.getBeansOfType(CloudFoundryOperations.class);
		beans.entrySet().forEach( entry ->{
			System.out.println("KEY "+entry.getKey());
			System.out.println("VALUE "+entry.getValue());
		});

	}

}
