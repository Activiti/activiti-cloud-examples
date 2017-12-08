package org.activiti.cloud.connectors.twitter;

import org.activiti.cloud.connectors.starter.configuration.EnableActivitiCloudConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@SpringBootApplication
@EnableActivitiCloudConnector
@ComponentScan({"org.activiti.cloud.connectors.starter", "org.activiti.cloud.connectors.twitter"})
@EnableScheduling
@EnableBinding(CampaignMessageChannels.class)
public class TwitterCloudConnectorApp implements CommandLineRunner {

    @Autowired
    private LangAwareTwitterStatusListener langAwareTwitterStatusListener;

    public static void main(String[] args) {
        SpringApplication.run(TwitterCloudConnectorApp.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(langAwareTwitterStatusListener);
        twitterStream.sample();
    }
}
