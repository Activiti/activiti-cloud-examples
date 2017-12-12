package org.activiti.cloud.connectors.ranking;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankingController {

    public static Map<String, List<RankedUser>> ranking = new ConcurrentHashMap<>();

    @RequestMapping(method = RequestMethod.GET, path = "/rank/{topic}")
    public List<RankedUser> getRanking(@PathParam("topic") String topic) {
        return ranking.get(topic);
    }

    public static class RankedUser {

        private int nroOfTweets;
        private String userName;

        public RankedUser() {
        }

        public RankedUser(int nroOfTweets,
                          String userName) {
            this.nroOfTweets = nroOfTweets;
            this.userName = userName;
        }

        public int getNroOfTweets() {
            return nroOfTweets;
        }

        public void setNroOfTweets(int nroOfTweets) {
            this.nroOfTweets = nroOfTweets;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public String toString() {
            return "RankedUser{" +
                    "nroOfTweets=" + nroOfTweets +
                    ", userName='" + userName + '\'' +
                    '}';
        }
    }
}

