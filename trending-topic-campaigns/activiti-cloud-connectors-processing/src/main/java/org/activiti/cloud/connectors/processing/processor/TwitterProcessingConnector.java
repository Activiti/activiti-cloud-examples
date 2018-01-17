package org.activiti.cloud.connectors.processing.processor;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.connectors.processing.ProcessingConnectorChannels;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static net.logstash.logback.marker.Markers.append;


@Component
@EnableBinding(ProcessingConnectorChannels.class)
public class TwitterProcessingConnector {

    private final Logger logger = LoggerFactory.getLogger(TwitterProcessingConnector.class);
    @Value("${spring.application.name}")
    private String appName;

    private final MessageChannel integrationResultsProducer;


    public TwitterProcessingConnector(MessageChannel integrationResultsProducer) {

        this.integrationResultsProducer = integrationResultsProducer;
    }

    @StreamListener(value = ProcessingConnectorChannels.TWITTER_PROCESSING_CONSUMER)
    public void processEnglish(IntegrationRequestEvent event) throws InterruptedException {

        String tweet = String.valueOf(event.getVariables().get("text"));
        logger.info(append("service-name", appName),"placeholder for doing cleaning/processing of posted content sized "+(tweet==null?"null":tweet.length()));
        //TODO: perform processing

        Map<String, Object> results = new HashMap<>();
        results.put("text",
                tweet);
        IntegrationResultEvent ire = IntegrationResultEventBuilder.resultFor(event)
                .withVariables(results)
                .build();
        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }

}
