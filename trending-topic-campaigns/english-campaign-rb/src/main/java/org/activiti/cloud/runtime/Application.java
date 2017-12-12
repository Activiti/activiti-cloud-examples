package org.activiti.cloud.runtime;

import org.activiti.cloud.runtime.service.TopicController;
import org.activiti.cloud.services.events.ProcessEngineChannels;
import org.activiti.cloud.starter.configuration.ActivitiRuntimeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ActivitiRuntimeBundle
@EnableBinding({ProcessEngineChannels.class, CampaignMessageChannels.class})
@EnableScheduling
public class Application implements CommandLineRunner {

    @Autowired
    private TopicController topicController;

    public static void main(String[] args) {
        SpringApplication.run(Application.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> Starting Campaing for Trending Topic " + topicController.getCurrentTopic());
    }
}