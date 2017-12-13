package org.activiti.cloud.connectors.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.activiti.cloud.connectors.starter.channels.CloudConnectorChannels;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class TweetRankConnector {

    @Autowired
    private MessageChannel integrationResultsProducer;

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Rank English Tweet'")
    public synchronized void processEnglish(IntegrationRequestEvent event) throws InterruptedException {

        String author = String.valueOf(event.getVariables().get("author"));
        String campaign = String.valueOf(event.getVariables().get("campaign"));
        String attitude = String.valueOf(event.getVariables().get("attitude"));
        String processedMessage = String.valueOf(event.getVariables().get("text"));

        System.out.println(">>> Just Received a Tweet from: " + author + " related to the campaign: " + campaign + " with attitude: " + attitude + " - > " + processedMessage);

        rankAuthorForCampaign(campaign,
                              author);

        Map<String, Object> results = new HashMap<>();

        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                                                                results);

        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }

    private void rankAuthorForCampaign(String campaign,
                                       String author) {
        List<RankingController.RankedUser> rankedUsersForCampaign = initCampaignRankings(campaign);

        rankAuthorInCampaign(rankedUsersForCampaign,
                             author,
                             campaign);
    }

    private void rankAuthorInCampaign(List<RankingController.RankedUser> rankedUsersForCampaign,
                                      String author,
                                      String campaign) {
        boolean userFoundInCampaignRanking = false;
        for (RankingController.RankedUser ru : rankedUsersForCampaign) {
            if (ru.getUserName().equals(author)) {
                ru.setNroOfTweets(ru.getNroOfTweets() + 1);
                userFoundInCampaignRanking = true;
            }
        }
        if (!userFoundInCampaignRanking) {
            RankingController.ranking.get(campaign).add(new RankingController.RankedUser(1,
                                                                                         author));
            RankingController.ranking.get(campaign).sort(new Comparator<RankingController.RankedUser>() {
                @Override
                public int compare(RankingController.RankedUser o1,
                                   RankingController.RankedUser o2) {
                    return o2.getNroOfTweets() - o1.getNroOfTweets();
                }
            });
        }
    }

    private List<RankingController.RankedUser> initCampaignRankings(String campaign) {
        List<RankingController.RankedUser> rankedUsersForCampaign = RankingController.ranking.get(campaign);
        if (rankedUsersForCampaign == null) {
            RankingController.ranking.put(campaign,
                                          new CopyOnWriteArrayList<>());
        }
        return rankedUsersForCampaign;
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Get Tweets Rank'")
    public synchronized void getRanks(IntegrationRequestEvent event) throws InterruptedException {

        String campaign = String.valueOf(event.getVariables().get("campaign"));
        int top = Integer.valueOf(event.getVariables().get("nroTopAuthors").toString());

        Map<String, Object> topAuthorsInCampaign = extractTopAuthorsFromCampaign(campaign,
                                                                                 top);

        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                                                                topAuthorsInCampaign);

        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }

    private Map<String, Object> extractTopAuthorsFromCampaign(String campaign,
                                                              int top) {
        List<RankingController.RankedUser> rankedUsers = RankingController.ranking.get(campaign);
        Map<String, Object> results = new HashMap<>();
        if (rankedUsers == null || rankedUsers.isEmpty()) {
            results.put("top",
                        Collections.EMPTY_LIST);
            return results;
        }
        if (rankedUsers.size() > top) {
            results.put("top",
                        new ArrayList<>(rankedUsers.subList(0,
                                                            top)));
        } else {
            results.put("top",
                        new ArrayList<>(rankedUsers));
        }

        return results;
    }
}