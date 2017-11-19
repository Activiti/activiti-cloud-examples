package org.activiti.cloud.connectors.ranking;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.connectors.starter.configuration.EnableActivitiCloudConnector;
import org.activiti.cloud.services.api.commands.StartProcessInstanceCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableActivitiCloudConnector
@ComponentScan({"org.activiti.cloud.connectors.starter", "org.activiti.cloud.connectors.ranking"})
@EnableScheduling
public class RankingCloudConnector implements CommandLineRunner {

    @Autowired
    private MessageChannel runtimeCmdProducer;

    public static void main(String[] args) {
        SpringApplication.run(RankingCloudConnector.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

    @Scheduled(fixedRate = 60000)
    public void timerMessageSource() {
        System.out.println("Printing (local) Ranking: ");
        for (String key : RankingController.ranking.keySet()) {
            for (RankingController.RankedUser ru : RankingController.ranking.get(key)) {
                System.out.println("Ranked User: " + ru);
            }
        }

        System.out.println("#################################################################################");
        System.out.println("#  Prize time!!! starting Prize Process");
        System.out.println("#################################################################################");
        Map<String, Object> vars = new HashMap<>();
        StartProcessInstanceCmd startProcessInstanceCmd = new StartProcessInstanceCmd("tweet-prize:1:01939d88-cd23-11e7-93c0-936684dc4390",
                                                                                      vars);
        runtimeCmdProducer.send(MessageBuilder.withPayload(startProcessInstanceCmd).build());
    }
}
