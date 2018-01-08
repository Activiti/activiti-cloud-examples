package org.activiti.cloud.connectors.processing.analyzer;

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
import static net.logstash.logback.marker.Markers.*;

@Component
public class TweetAnalyzerConnector {

    private final Logger logger = LoggerFactory.getLogger(TweetAnalyzerConnector.class);
    @Value("${spring.application.name}")
    private String appName;

    private final MessageChannel integrationResultsProducer;

    public TweetAnalyzerConnector(MessageChannel integrationResultsProducer) {
        this.integrationResultsProducer = integrationResultsProducer;
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Analyze English Tweet'")
    public synchronized void analyzeEnglishTweet(IntegrationRequestEvent event) throws InterruptedException {

        String tweet = String.valueOf(event.getVariables().get("text"));

        Map<String, Object> results = new HashMap<>();

        // based on http://rahular.com/twitter-sentiment-analysis/
        // note you get a lot of 1s but there are some zeros if you search for "attitude: 0"
        results.put("attitude",
                NLP.findSentiment(tweet));

        logger.info(append("service-name", appName),event.getExecutionId()+" analyzed tweet with sentiment "+results.get("attitude"));

        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                                                                results);

        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }
}
