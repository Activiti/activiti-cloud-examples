package org.activiti.cloud.connectors.twitter;

import org.activiti.cloud.connectors.twitter.model.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(LangAwareTwitterStatusListener.class);

    private final MessageChannel campaignProducer;

    public LangAwareTwitterStatusListener(MessageChannel campaignProducer) {
        this.campaignProducer = campaignProducer;
    }

    public void onStatus(Status status) {

        Tweet t = new Tweet(status.getText(),
                            status.getUser().getScreenName(),
                            status.getLang());
        logger.info("> Tweet: " + status.getText());
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
    public void onScrubGeo(long userId, long upToStatusId) {
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
    }

    public void onException(Exception ex) {
        ex.printStackTrace();
    }
}
