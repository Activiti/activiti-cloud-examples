package org.activiti.cloud.connectors.twitter;

import org.activiti.cloud.connectors.twitter.model.Tweet;
import org.activiti.cloud.connectors.twitter.model.TweetEntity;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
@EnableBinding(CampaignMessageChannels.class)
public class DummyTweeter {

    private final MessageChannel campaignProducer;
    private final TweetRepository tweetRepository;

    public DummyTweeter(MessageChannel campaignProducer, TweetRepository tweetRepository){
        this.campaignProducer = campaignProducer;
        this.tweetRepository = tweetRepository;
    }

    @Scheduled(fixedRateString = "${tweetrate}")
    public void startProcessWithTweet(){

        TweetEntity tweetEntity = getRandomTweet();

        Tweet t = new Tweet(tweetEntity.getText(),tweetEntity.getAuthor(),tweetEntity.getLang());

        campaignProducer.send(MessageBuilder.withPayload(t).setHeader("lang",
                t.getLang()).build());
    }

    public TweetEntity getRandomTweet(){
        long count = tweetRepository.count();
        int countInt = Integer.MAX_VALUE;
        if(count<Integer.MAX_VALUE){
            countInt = (int)count;
        }
        Random random = new Random();
        int id = random.nextInt(countInt)+1;
        return tweetRepository.findById(new Long(id)).get();
    }

}
