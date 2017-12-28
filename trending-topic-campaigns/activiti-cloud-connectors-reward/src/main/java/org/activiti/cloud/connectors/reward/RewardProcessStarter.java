package org.activiti.cloud.connectors.reward;

import org.activiti.cloud.connectors.reward.model.Campaign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableBinding(RewardMessageChannels.class)
public class RewardProcessStarter {

    @Value("${campaignCycle1}")
    private String cycle1Campaigns;

    private final MessageChannel rewardProducer;

    public RewardProcessStarter(MessageChannel rewardProducer){
        this.rewardProducer = rewardProducer;
    }

    @Scheduled(fixedRate = 60000)
    public void triggerRewardProcessForCycle1Campaigns() {
        if(cycle1Campaigns!=null){
            sendMessageForCampaigns(cycle1Campaigns);
        }
    }

    private void sendMessageForCampaigns(String campaigns) {
        String[] campaignArray = campaigns.split(",");
        for(String campaign:campaignArray){
            String[] splitCampaignString = campaign.split("-");
            String name = splitCampaignString[0];
            String lang = splitCampaignString[1];
            rewardProducer.send(MessageBuilder.withPayload(new Campaign(name,lang)).setHeader("lang",lang).setHeader("campaign",name).build());
        }
    }
}
