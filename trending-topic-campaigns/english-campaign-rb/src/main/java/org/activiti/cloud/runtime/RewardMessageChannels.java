package org.activiti.cloud.runtime;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface RewardMessageChannels {
    String REWARD_CHANNEL = "rewardConsumer";

    @Input(REWARD_CHANNEL)
    SubscribableChannel rewardConsumer();
}
