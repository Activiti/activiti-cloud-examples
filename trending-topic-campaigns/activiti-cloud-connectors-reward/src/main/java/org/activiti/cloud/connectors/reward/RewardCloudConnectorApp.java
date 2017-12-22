package org.activiti.cloud.connectors.reward;

import org.activiti.cloud.connectors.starter.configuration.EnableActivitiCloudConnector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableActivitiCloudConnector
@ComponentScan({"org.activiti.cloud.connectors.starter", "org.activiti.cloud.connectors.reward"})
public class RewardCloudConnectorApp implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(RewardCloudConnectorApp.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
