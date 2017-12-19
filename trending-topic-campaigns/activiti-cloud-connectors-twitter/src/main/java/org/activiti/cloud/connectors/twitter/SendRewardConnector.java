package org.activiti.cloud.connectors.twitter;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.connectors.starter.channels.CloudConnectorChannels;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

@Component

public class SendRewardConnector {

    private Logger logger = LoggerFactory.getLogger(SendRewardConnector.class);

    private final MessageChannel integrationResultsProducer;

    public SendRewardConnector(MessageChannel integrationResultsProducer) {
        this.integrationResultsProducer = integrationResultsProducer;
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='SendRewardToWinners'")
    public void tweet(IntegrationRequestEvent event) throws TwitterException {
        Map<String, Object> results = new HashMap<>();

        logger.info("#################################################################################");
        logger.info("#  Reward time!!! You WON!!! ");
        logger.info(" I'm tweeting to a Winner: " + event.getVariables().get("winner") + " \n");
        logger.info("#################################################################################");

        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                                                                results);

        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }
}
