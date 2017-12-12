package org.activiti.cloud.runtime;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.runtime.model.Tweet;
import org.activiti.cloud.runtime.service.TopicController;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnglishCampaign {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TopicController topicController;

    @StreamListener(value = CampaignMessageChannels.CAMPAIGN_CHANNEL, condition = "headers['lang']=='en'")
    public void tweet(Tweet tweet) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("text",
                 tweet.getText());
        vars.put("author",
                 tweet.getAuthor());
        vars.put("campaign",
                 topicController.getCurrentTopic());
        runtimeService.startProcessInstanceByKey("launchCampaign",
                                                 vars);
    }

    @Scheduled(fixedRate = 60000)
    public void triggerPrizeProcessForCampaign() {
        System.out.println("Starting Prize For Campaign: " + topicController.getCurrentTopic());
        Map<String, Object> vars = new HashMap<>();
        vars.put("campaign",
                 topicController.getCurrentTopic());
        vars.put("nroTopAuthors",
                 3);
        runtimeService.startProcessInstanceByKey("tweet-prize",
                                                 vars);
    }
}
