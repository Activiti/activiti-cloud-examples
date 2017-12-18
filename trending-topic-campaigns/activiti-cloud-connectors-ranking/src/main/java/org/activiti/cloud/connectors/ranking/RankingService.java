/*
 * Copyright 2017 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.connectors.ranking;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class RankingService {

    private Map<String, List<RankedAuthor>> ranking = new ConcurrentHashMap<>();

    public List<RankedAuthor> rank(String topic,
                                   String username) {
        List<RankedAuthor> rankedUsers = getCurrentRankedUsers(topic);
        Optional<RankedAuthor> researchedUser = rankedUsers.stream()
                .filter(user -> user.getUserName().equals(username))
                .findFirst();

        if (researchedUser.isPresent()) {
            researchedUser.get().incrementTweets();
            rankedUsers.sort(
                    (o1, o2) ->
                            o2.getNroOfTweets() - o1.getNroOfTweets());
        } else {
            rankedUsers.add(new RankedAuthor(username));
        }
        return ranking.get(topic);
    }

    private List<RankedAuthor> getCurrentRankedUsers(String topic) {
        if (!ranking.containsKey(topic)) {
            ranking.put(topic,
                        new CopyOnWriteArrayList<>());
        }
        return ranking.get(topic);
    }

    public List<RankedAuthor> getRanking(String topic) {
        return Collections.unmodifiableList(getCurrentRankedUsers(topic));
    }

    public Map<String, List<RankedAuthor>> getRanking() {
        return Collections.unmodifiableMap(ranking);
    }

    public List<RankedAuthor> getTop(String topic, int topSize) {
        List<RankedAuthor> top = getCurrentRankedUsers(topic)
                .stream()
                .limit(topSize)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(top);
    }
}
