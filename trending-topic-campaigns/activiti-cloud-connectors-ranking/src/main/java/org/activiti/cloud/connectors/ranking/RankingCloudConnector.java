package org.activiti.cloud.connectors.ranking;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.connectors.starter.configuration.EnableActivitiCloudConnector;
import org.activiti.cloud.services.api.commands.StartProcessInstanceCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        if(RankingController.ranking == null || RankingController.ranking.keySet() == null || RankingController.ranking.keySet().isEmpty()){
            System.out.println("No ranking set");
        }
        for (String key : RankingController.ranking.keySet()) {
            System.out.println("Campaign being ranked is "+key);
            for (RankingController.RankedUser ru : RankingController.ranking.get(key)) {
                System.out.println("Ranked User: " + ru);
            }
        }
    }
}
