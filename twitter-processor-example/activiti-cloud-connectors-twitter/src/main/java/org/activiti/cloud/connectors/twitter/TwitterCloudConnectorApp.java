package org.activiti.cloud.connectors.twitter;

import org.activiti.cloud.connectors.starter.configuration.EnableActivitiCloudConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@SpringBootApplication
@EnableActivitiCloudConnector
@ComponentScan({"org.activiti.cloud.connectors.starter", "org.activiti.cloud.connectors.twitter"})
@EnableScheduling
public class TwitterCloudConnectorApp implements CommandLineRunner {

    @Autowired
    private LanguageMappingsController languageMappingsController;

    @Autowired
    private LangAwareTwitterStatusListener langAwareTwitterStatusListener;

    public static void main(String[] args) {
        SpringApplication.run(TwitterCloudConnectorApp.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {

        initialize();

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(langAwareTwitterStatusListener);
        twitterStream.sample();
    }

    private void initialize() {
        languageMappingsController.setLanguageMapping("en",
                                                      "processDefIdHere");
    }
}
