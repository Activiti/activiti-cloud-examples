package org.activiti.cloud.connectors.reward;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface RewardMessageChannels {
    String REWARD_CHANNEL = "rewardProducer";

    @Output(REWARD_CHANNEL)
    MessageChannel rewardProducer();
}
