package org.activiti.cloud.connectors.ranking;

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
@EnableBinding(RankingConnectorChannels.class)
public class UpdateAuthorRankConnector {

    private Logger logger = LoggerFactory.getLogger(UpdateAuthorRankConnector.class);
    @Value("${spring.application.name}")
    private String appName;

    private final IntegrationResultSender integrationResultSender;

    private final RankingService rankingService;

    public UpdateAuthorRankConnector(IntegrationResultSender integrationResultSender,
                                     RankingService rankingService) {
        this.integrationResultSender = integrationResultSender;
        this.rankingService = rankingService;
    }

    @StreamListener(value = RankingConnectorChannels.UPDATE_RANK_CONSUMER)
    public void processEnglish(IntegrationRequestEvent event) throws InterruptedException {

        String author = String.valueOf(event.getVariables().get("author"));
        String campaign = String.valueOf(event.getVariables().get("campaign"));
        String attitude = String.valueOf(event.getVariables().get("attitude"));
        String processedMessage = String.valueOf(event.getVariables().get("text"));

        logger.info(append("service-name",
                           appName),
                    "rank author >>> Received a Tweet from: " + author + " related to the campaign: " + campaign + " with attitude/sentiment score: " + attitude + " - > " + processedMessage);

        rankingService.rank(campaign,
                            author);

        Map<String, Object> results = new HashMap<>();

        Message<IntegrationResultEvent> message = IntegrationResultEventBuilder.resultFor(event)
                .withVariables(results)
                .buildMessage();
        integrationResultSender.send(message);
    }

    @StreamListener(value = RankingConnectorChannels.GET_RANK_CONSUMER)
    public void getRanks(IntegrationRequestEvent event) throws InterruptedException {

        String campaign = String.valueOf(event.getVariables().get("campaign"));
        int top = Integer.valueOf(event.getVariables().get("nroTopAuthors").toString());

        Map<String, Object> topAuthorsInCampaign = extractTopAuthorsFromCampaign(campaign,
                                                                                 top);

        Message<IntegrationResultEvent> message = IntegrationResultEventBuilder.resultFor(event)
                .withVariables(topAuthorsInCampaign)
                .buildMessage();
        integrationResultSender.send(message);
    }

    private Map<String, Object> extractTopAuthorsFromCampaign(String campaign,
                                                              int top) {
        Map<String, Object> results = new HashMap<>();
        results.put("top",
                    rankingService.getTop(campaign,
                                          top));
        return results;
    }
}