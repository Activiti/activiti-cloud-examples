package org.activiti.cloud.runtime;

import org.activiti.cloud.runtime.service.TopicController;
import org.activiti.cloud.services.events.ProcessEngineChannels;
import org.activiti.cloud.starter.configuration.ActivitiRuntimeBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableScheduling;

import static net.logstash.logback.marker.Markers.append;

@SpringBootApplication
@ActivitiRuntimeBundle
@EnableBinding({ProcessEngineChannels.class, CampaignMessageChannels.class, RewardMessageChannels.class})
@EnableScheduling
public class Application implements CommandLineRunner {

    @Value("${spring.application.name}")
    private String appName;
    private Logger logger = LoggerFactory.getLogger(Application.class);

    private final TopicController topicController;

    public Application(TopicController topicController) {
        this.topicController = topicController;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info(append("service-name", appName), ">>> Starting Campaing for Trending Topic " + topicController.getCurrentTopic());
    }
}