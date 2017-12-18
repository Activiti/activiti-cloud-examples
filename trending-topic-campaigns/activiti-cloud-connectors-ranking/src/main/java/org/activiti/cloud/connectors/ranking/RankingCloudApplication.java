package org.activiti.cloud.connectors.ranking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@ComponentScan({"org.activiti.cloud.connectors.starter", "org.activiti.cloud.connectors.ranking"})
@EnableScheduling
public class RankingCloudApplication implements CommandLineRunner {

    private final RankingService rankingService;

    public RankingCloudApplication(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(RankingCloudApplication.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

    @Scheduled(fixedRate = 60000)
    public void logCurrentRankingsForAllCampaigns() {
        System.out.println("Printing (local) Ranking: ");
        if(rankingService.getRanking().keySet().isEmpty()){
            System.out.println("No ranking set");
        }
        for (String key : rankingService.getRanking().keySet()) {
            System.out.println("Campaign being ranked is "+key);
            for (RankedAuthor ru : rankingService.getRanking(key)) {
                System.out.println("Ranked User: " + ru);
            }
        }
    }
}
