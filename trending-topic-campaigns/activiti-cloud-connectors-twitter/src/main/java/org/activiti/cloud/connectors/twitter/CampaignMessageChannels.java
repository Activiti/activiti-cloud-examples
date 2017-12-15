package org.activiti.cloud.connectors.twitter;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CampaignMessageChannels {

    String CAMPAIGN_CHANNEL = "campaignProducer";

    @Output(CAMPAIGN_CHANNEL)
    MessageChannel campaignProducer();
}
