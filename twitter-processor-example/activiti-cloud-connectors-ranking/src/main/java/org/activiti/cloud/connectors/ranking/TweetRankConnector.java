package org.activiti.cloud.connectors.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.cloud.connectors.starter.channels.CloudConnectorChannels;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TweetRankConnector {

    @Autowired
    private MessageChannel integrationResultsProducer;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Rank Tweet'")
    public synchronized void processEnglish(IntegrationRequestEvent event) throws InterruptedException {

        //  System.out.println("Just recieved an integration request event: " + event);
        String user = String.valueOf(event.getVariables().get("user"));
        String filterApplied = String.valueOf(event.getVariables().get("filterApplied"));
        List<RankingController.RankedUser> rankedUsersForFilter = RankingController.ranking.get(filterApplied);
        if (rankedUsersForFilter == null) {
            RankingController.ranking.put(filterApplied,
                                          new ArrayList<>());
        }
        List<RankingController.RankedUser> rankedUsers = RankingController.ranking.get(filterApplied);
        boolean found = false;
        for (RankingController.RankedUser ru : rankedUsers) {
            if (ru.getUserName().equals(user)) {
                ru.setNroOfTweets(ru.getNroOfTweets() + 1);
                found = true;
            }
        }
        if (!found) {
            RankingController.ranking.get(filterApplied).add(new RankingController.RankedUser(1,
                                                                                              user));
            RankingController.ranking.get(filterApplied).sort(new Comparator<RankingController.RankedUser>() {
                @Override
                public int compare(RankingController.RankedUser o1,
                                   RankingController.RankedUser o2) {
                    return o2.getNroOfTweets() - o1.getNroOfTweets();
                }
            });
        }

        String message = String.valueOf(event.getVariables().get("shout"));

        System.out.println(">>> I should rank this tweet: " + message);

        Map<String, Object> results = new HashMap<>();

        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                                                                results);

        //System.out.println("I'm sending back an integratrion Result: " + ire);
        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Get Tweets Rank'")
    public synchronized void getRanks(IntegrationRequestEvent event) throws InterruptedException {

//        String filter = "TRUMP";
        String filter = "OXFORD";
        List<RankingController.RankedUser> rankedUsers = RankingController.ranking.get(filter);

        Map<String, Object> results = new HashMap<>();
        if (rankedUsers != null) {
            if (rankedUsers.size() > 3) {
                results.put("top3",
                            rankedUsers.subList(0,
                                                3));
            } else {
                results.put("top3",
                            rankedUsers);
            }
        } else {
            results.put("top3",
                        Collections.EMPTY_LIST);
        }

        IntegrationResultEvent ire = new IntegrationResultEvent(UUID.randomUUID().toString(),
                                                                event.getExecutionId(),
                                                                results);

        //System.out.println("I'm sending back an integratrion Result: " + ire);
        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }
}