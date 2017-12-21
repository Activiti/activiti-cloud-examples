package org.activiti.cloud.connectors.external.processor;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.connectors.starter.channels.CloudConnectorChannels;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static net.logstash.logback.marker.Markers.append;


@Component
public class TwitterProcessingConnector {

    private final Logger logger = LoggerFactory.getLogger(TwitterProcessingConnector.class);
    @Value("${spring.application.name}")
    private String appName;

    private final MessageChannel integrationResultsProducer;


    public TwitterProcessingConnector(MessageChannel integrationResultsProducer) {

        this.integrationResultsProducer = integrationResultsProducer;
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Process English Tweet'")
    public synchronized void processEnglish(IntegrationRequestEvent event) throws InterruptedException {

        String tweet = String.valueOf(event.getVariables().get("text"));
        logger.debug(append("service-name", appName),"placeholder for doing cleaning/processing of posted content sized "+(tweet==null?"null":tweet.length()));
        //TODO: perform processing


        Map<String, Object> results = new HashMap<>();
        results.put("text",
                tweet);
        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                results);
        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }

}
