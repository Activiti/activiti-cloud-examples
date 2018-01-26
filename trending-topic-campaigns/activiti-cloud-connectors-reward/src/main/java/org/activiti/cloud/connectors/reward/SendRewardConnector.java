package org.activiti.cloud.connectors.reward;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.connectors.starter.channels.IntegrationResultSender;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static net.logstash.logback.marker.Markers.append;

@Component
@EnableBinding(RewardMessageChannels.class)
public class SendRewardConnector {

    private Logger logger = LoggerFactory.getLogger(SendRewardConnector.class);
    @Value("${spring.application.name}")
    private String appName;

    private final IntegrationResultSender integrationResultSender;

    public SendRewardConnector(IntegrationResultSender integrationResultSender) {
        this.integrationResultSender = integrationResultSender;
    }

    @StreamListener(value = RewardMessageChannels.REWARD_CONSUMER)
    public void tweet(IntegrationRequestEvent event) {
        Map<String, Object> results = new HashMap<>();
        Collection winners = (Collection) event.getVariables().get("top");
        String campaign = String.valueOf(event.getVariables().get("campaign"));

        for (Object winner : winners) {
            logger.info(append("service-name",
                               appName),
                        "#" + campaign + "#################################################################");
            logger.info(append("service-name",
                               appName),
                        "#  Reward time!!! You WON " + winner + "!!! ");
            logger.info(append("service-name",
                               appName),
                        "#################################################################################");
        }

        Message<IntegrationResultEvent> message = IntegrationResultEventBuilder.resultFor(event)
                .withVariables(results)
                .buildMessage();

        integrationResultSender.send(message);
    }
}
