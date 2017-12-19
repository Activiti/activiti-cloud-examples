package org.activiti.cloud.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.runtime.model.Tweet;
import org.activiti.cloud.runtime.service.TopicController;
import org.activiti.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnglishCampaign {

    private Logger logger = LoggerFactory.getLogger(EnglishCampaign.class);

    private final RuntimeService runtimeService;

    private final TopicController topicController;

    public EnglishCampaign(RuntimeService runtimeService,
                           TopicController topicController) {
        this.runtimeService = runtimeService;
        this.topicController = topicController;
    }

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
        logger.info("Starting Prize For Campaign: " + topicController.getCurrentTopic());
        Map<String, Object> vars = new HashMap<>();
        vars.put("campaign",
                 topicController.getCurrentTopic());
        vars.put("nroTopAuthors",
                 3);
        vars.put("top",
                 new ArrayList<>());
        runtimeService.startProcessInstanceByKey("tweet-prize",
                                                 vars);
    }
}
