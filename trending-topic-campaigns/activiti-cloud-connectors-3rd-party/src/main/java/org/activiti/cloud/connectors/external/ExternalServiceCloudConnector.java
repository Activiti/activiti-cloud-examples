package org.activiti.cloud.connectors.external;

import org.activiti.cloud.connectors.starter.configuration.EnableActivitiCloudConnector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableActivitiCloudConnector
@ComponentScan({"org.activiti.cloud.connectors.starter", "org.activiti.cloud.connectors.external"})
public class ExternalServiceCloudConnector implements CommandLineRunner {

    @Value("${sla.requests}")
    private int requestPerMinute;

    @Value("${sla.enabled}")
    private boolean slaEnabled;

    public static void main(String[] args) {
        SpringApplication.run(ExternalServiceCloudConnector.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting SLA Requests with: RPM -> " + requestPerMinute + " and Enabled: " + slaEnabled);
    }
}
