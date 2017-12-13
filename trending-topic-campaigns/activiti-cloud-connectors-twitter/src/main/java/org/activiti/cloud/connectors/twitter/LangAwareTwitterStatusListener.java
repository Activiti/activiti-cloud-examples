package org.activiti.cloud.connectors.twitter;

import org.activiti.cloud.connectors.twitter.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

@Component
@EnableBinding(CampaignMessageChannels.class)
public class LangAwareTwitterStatusListener implements StatusListener {

    @Autowired
    private MessageChannel campaignProducer;

    public void onStatus(Status status) {

        Tweet t = new Tweet(status.getText(),
                            status.getUser().getScreenName(),
                            status.getLang());
        System.out.println("> Tweet: " + status.getText());
        processTwitterWithCampaigns(t);
    }

    private void processTwitterWithCampaigns(Tweet t) {
        campaignProducer.send(MessageBuilder.withPayload(t).setHeader("lang",
                                                                      t.getLang()).build());
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
    }

    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
    }

    @Override
    public void onScrubGeo(long l,
                           long l1) {
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
    }

    public void onException(Exception ex) {
        ex.printStackTrace();
    }
}
