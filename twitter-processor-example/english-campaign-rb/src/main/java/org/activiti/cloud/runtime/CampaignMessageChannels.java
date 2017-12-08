package org.activiti.cloud.runtime;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CampaignMessageChannels {
    String CAMPAIGN_CHANNEL = "campaignConsumer";

    @Input(CAMPAIGN_CHANNEL)
    SubscribableChannel campaignConsumer();
}
