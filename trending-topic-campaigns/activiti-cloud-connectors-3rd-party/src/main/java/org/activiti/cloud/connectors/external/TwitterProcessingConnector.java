package org.activiti.cloud.connectors.external;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.connectors.external.model.Shout;
import org.activiti.cloud.connectors.starter.channels.CloudConnectorChannels;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
public class TwitterProcessingConnector {

    private final MessageChannel integrationResultsProducer;

    private final ShoutServiceClientSelector shoutServiceClientSelector;

    public TwitterProcessingConnector(MessageChannel integrationResultsProducer,
                                      ShoutServiceClientSelector shoutServiceClientSelector) {

        this.integrationResultsProducer = integrationResultsProducer;
        this.shoutServiceClientSelector = shoutServiceClientSelector;
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Process English Tweet'")
    public synchronized void processEnglish(IntegrationRequestEvent event) throws InterruptedException {

        String tweet = String.valueOf(event.getVariables().get("text"));
        Shout shout = shoutServiceClientSelector.select().shout(tweet);
        completeIntegrationRequest(event,
                                   shout);
    }

    private void completeIntegrationRequest(IntegrationRequestEvent event,
                                            Shout shout) {
        Map<String, Object> results = new HashMap<>();

        results.put("text",
                    shout.getOUTPUT());

        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                                                                results);

        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }
}
