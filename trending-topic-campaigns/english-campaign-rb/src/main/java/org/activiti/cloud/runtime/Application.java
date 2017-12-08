package org.activiti.cloud.runtime;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.runtime.model.Tweet;
import org.activiti.cloud.runtime.service.FilterController;
import org.activiti.cloud.services.events.ProcessEngineChannels;
import org.activiti.cloud.starter.configuration.ActivitiRuntimeBundle;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ActivitiRuntimeBundle
@EnableBinding({ProcessEngineChannels.class, CampaignMessageChannels.class})
public class Application implements CommandLineRunner {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FilterController filter;

    public static void main(String[] args) {
        SpringApplication.run(Application.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> Starting Campaing for Trending Topic " + filter.getCurrentFilter());
    }

    @StreamListener(value = CampaignMessageChannels.CAMPAIGN_CHANNEL, condition = "headers['lang']=='en'")
    public void tweet(Tweet tweet) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("text",
                 tweet.getText());
        vars.put("author",
                 tweet.getAuthor());
        vars.put("campaign",
                 filter.getCurrentFilter());
        runtimeService.startProcessInstanceByKey("launchCampaign",
                                                 vars);
    }


}