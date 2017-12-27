package org.activiti.cloud.connectors.twitter;

import org.activiti.cloud.connectors.starter.configuration.EnableActivitiCloudConnector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableActivitiCloudConnector
@ComponentScan({"org.activiti.cloud.connectors.starter", "org.activiti.cloud.connectors.twitter"})
@EnableScheduling
public class TwitterCloudConnectorApp implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(TwitterCloudConnectorApp.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
