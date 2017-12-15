package org.activiti.cloud.connectors.ranking;

import java.util.List;
import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankingController {


    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/rank/{topic}")
    public List<RankedUser> getRanking(@PathParam("topic") String topic) {
        return rankingService.getRanking(topic);
    }

}

