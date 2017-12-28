package org.activiti.cloud.runtime;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.cloud.runtime.model.Campaign;
import org.activiti.cloud.runtime.model.Tweet;
import org.activiti.cloud.runtime.service.TopicController;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
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

    @StreamListener(value = CampaignMessageChannels.CAMPAIGN_CHANNEL, condition = "headers['lang']=='${campaign.lang}'")
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

    @StreamListener(value = RewardMessageChannels.REWARD_CHANNEL, condition = "headers['lang']=='${campaign.lang}' and headers['campaign']=='${campaign.topic}'")
    public void startPrizeProcess(Campaign campaign) {
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


    @Scheduled(fixedRate = 60000)
    public void logExecutions() throws UnknownHostException {
        List<Execution> executionList = runtimeService.createExecutionQuery().list();
        logger.info("There are "+executionList.size()+" process executions in RB instance on host "+ InetAddress.getLocalHost().getHostName());
    }
}
