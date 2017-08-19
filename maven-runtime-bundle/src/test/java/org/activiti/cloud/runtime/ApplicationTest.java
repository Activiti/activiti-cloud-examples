package org.activiti.cloud.runtime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext
public class ApplicationTest {

	@Value("${spring.activiti.process-definition-location-prefix:}")
	private String procPath;

	//for test purposes the env variable ACT_RB_PROCESSES_PATH is as set in pom.xml using surefire
	@Test
	public void contextLoads() throws Exception {
		//check variable has been resolved for path to processes
		//first check it's not the default
		Assert.assertNotEquals(procPath,"file:/processes/");
		Assert.assertEquals(procPath,("file:"+System.getenv("ACT_RB_PROCESSES_PATH")));
	}

}