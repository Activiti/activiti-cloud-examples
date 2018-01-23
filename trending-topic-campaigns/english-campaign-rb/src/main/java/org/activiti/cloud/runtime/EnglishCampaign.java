package org.activiti.cloud.runtime;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.runtime.model.Campaign;
import org.activiti.cloud.runtime.model.Tweet;
import org.activiti.cloud.runtime.service.TopicController;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnglishCampaign {

    private Logger logger = LoggerFactory.getLogger(EnglishCampaign.class);

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    private final TopicController topicController;

    public EnglishCampaign(RuntimeService runtimeService,
                           TopicController topicController,
                           TaskService taskService) {
        this.runtimeService = runtimeService;
        this.topicController = topicController;
        this.taskService = taskService;
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
        logger.info("There are "+runtimeService.createExecutionQuery().count()+" process executions in RB instance on host "+ InetAddress.getLocalHost().getHostName());
        logger.info("There are "+runtimeService.createProcessInstanceQuery().processDefinitionKey("launchCampaign").count()+" launchCampaign process instances in RB instance on host "+ InetAddress.getLocalHost().getHostName());
        logger.info("There are "+runtimeService.createProcessInstanceQuery().processDefinitionKey("tweet-prize").count()+" tweet-prize process instances in RB instance on host "+ InetAddress.getLocalHost().getHostName());
        logger.info("There are "+taskService.createTaskQuery().count()+" tasks in RB instance on host "+ InetAddress.getLocalHost().getHostName());
/*        List<ProcessInstance> procInstanceList = runtimeService.createProcessInstanceQuery().orderByProcessInstanceId().asc().includeProcessVariables().list();
        for(ProcessInstance instance:procInstanceList) {
            logger.debug("Inspecting open process instance with id " + instance.getProcessInstanceId() + " for " + instance.getProcessDefinitionKey() + " started at " + instance.getStartTime());
            logger.debug("Instance suspended? " + instance.isSuspended()+" Instance ended? " + instance.isEnded());
            if (instance.getProcessVariables() != null && instance.getProcessVariables().size() > 0) {
                for (String varKey : instance.getProcessVariables().keySet()) {
                    logger.debug(instance.getProcessInstanceId()+" Variable - " + varKey + " - " + instance.getProcessVariables().get(varKey));
                }
            }
        }

        List<Execution> processExecutions = runtimeService.createExecutionQuery().list();
        for(Execution execution:processExecutions){
            logger.debug("Inspecting open process execution with id " + execution.getId() + " for proc inst " + execution.getRootProcessInstanceId());
            logger.debug("ParentId " + execution.getParentId()+" SuperExecutionId "+execution.getSuperExecutionId());
            logger.debug("Execution suspended? " + execution.isSuspended()+" Execution ended? " + execution.isEnded());
        }*/
    }
}
