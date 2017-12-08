package org.activiti.cloud.connectors.twitter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TwitterCloudConnectorApp.class)

public class ApplicationTest {

    //for test purposes the env variable ACT_RB_PROCESSES_PATH is as set in pom.xml using surefire
    @Test
    public void contextLoads() throws Exception {
        //check variable has been resolved for path to processes
        //first check it's not the default

    }
}