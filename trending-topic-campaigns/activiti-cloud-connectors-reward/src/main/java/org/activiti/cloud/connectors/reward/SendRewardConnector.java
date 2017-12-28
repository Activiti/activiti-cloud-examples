package org.activiti.cloud.connectors.reward;

import java.util.Collection;
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

public class SendRewardConnector {

    private Logger logger = LoggerFactory.getLogger(SendRewardConnector.class);
    @Value("${spring.application.name}")
    private String appName;
    private final MessageChannel integrationResultsProducer;

    public SendRewardConnector(MessageChannel integrationResultsProducer) {
        this.integrationResultsProducer = integrationResultsProducer;
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='SendRewardToWinners'")
    public void tweet(IntegrationRequestEvent event)  {
        Map<String, Object> results = new HashMap<>();
        Collection winners = (Collection) event.getVariables().get("top");

        for(Object winner:winners){
            logger.info(append("service-name", appName),"#################################################################################");
            logger.info(append("service-name", appName),"#  Reward time!!! You WON!!! ");
            logger.info(append("service-name", appName)," I'm tweeting to a Winner: " + winner + " \n");
            logger.info(append("service-name", appName),"#################################################################################");
        }

        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                                                                results);

        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }
}
