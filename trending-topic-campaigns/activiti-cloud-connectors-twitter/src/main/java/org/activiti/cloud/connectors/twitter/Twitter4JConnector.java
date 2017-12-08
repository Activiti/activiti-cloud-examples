package org.activiti.cloud.connectors.twitter;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.connectors.starter.channels.CloudConnectorChannels;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

@Component
public class Twitter4JConnector {

    @Autowired
    private MessageChannel integrationResultsProducer;

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Tweet'")
    public void tweet(IntegrationRequestEvent event) throws TwitterException {
        //Twitter twitter = TwitterFactory.getSingleton();

        //System.out.println("Just received an integration request event: " + event);

        Map<String, Object> results = new HashMap<>();

        System.out.println("#################################################################################");
        System.out.println("#  Prize time!!! You WON!!! ");
        System.out.println(" I'm tweeting to a Winner: " + event.getVariables().get("winner") + " \n");
        System.out.println("#################################################################################");

        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                                                                results);

        //  System.out.println("I'm sending back an integratrion Result: " + ire);
        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }
}
