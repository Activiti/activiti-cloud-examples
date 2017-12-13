package org.activiti.cloud.connectors.external;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.connectors.starter.channels.CloudConnectorChannels;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class TweetAnalyzerConnector {

    private final MessageChannel integrationResultsProducer;

    public TweetAnalyzerConnector(MessageChannel integrationResultsProducer) {
        this.integrationResultsProducer = integrationResultsProducer;
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Analyze English Tweet'")
    public synchronized void analyzeEnglishTweet(IntegrationRequestEvent event) throws InterruptedException {

        String tweet = String.valueOf(event.getVariables().get("text"));

        Map<String, Object> results = new HashMap<>();

        results.put("attitude",
                    "positive");

        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                                                                results);

        //  System.out.println("I'm sending back an integratrion Result: " + ire);
        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }
}
