package org.activiti.cloud.connectors.twitter;

import org.activiti.cloud.connectors.starter.configuration.EnableActivitiCloudConnector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import twitter4j.TwitterStream;

@SpringBootApplication
@EnableActivitiCloudConnector
@ComponentScan({"org.activiti.cloud.connectors.starter", "org.activiti.cloud.connectors.twitter"})
public class TwitterCloudConnectorApp implements CommandLineRunner {

    private final LangAwareTwitterStatusListener langAwareTwitterStatusListener;

    private final TwitterStreamProvider streamProvider;

    public TwitterCloudConnectorApp(LangAwareTwitterStatusListener langAwareTwitterStatusListener,
                                    TwitterStreamProvider streamProvider) {
        this.langAwareTwitterStatusListener = langAwareTwitterStatusListener;
        this.streamProvider = streamProvider;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterCloudConnectorApp.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {
        TwitterStream twitterStream = streamProvider.getStreamInstance();
        twitterStream.addListener(langAwareTwitterStatusListener);
        twitterStream.sample();
    }
}
