/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
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

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class RankingServiceTest {

    private static final String TOPIC = "topic1";
    private static final String JOHN = "john";
    private static final String PETER = "peter";
    private static final String PAUL = "paul";

    private RankingService rankingService = new RankingService();

    @Test
    public void rankShouldAddUserWhenNotExist() throws Exception {
        //when
        List<RankedAuthor> rank = rankingService.rank(TOPIC,
                                                    JOHN);

        //then
        assertThat(rank).isNotNull();
        assertThat(rank).extracting(RankedAuthor::getUserName,
                RankedAuthor::getNroOfTweets)
                .containsExactly(tuple(JOHN,
                                       1));
    }

    @Test
    public void rankShouldIncrementTweetsWhenUserAlreadyExists() throws Exception {
        //given
        rankingService.rank(TOPIC,
                            JOHN);

        //when
        List<RankedAuthor> rank = rankingService.rank(TOPIC,
                                                    JOHN);

        //then
        assertThat(rank).isNotNull();
        assertThat(rank).extracting(RankedAuthor::getUserName,
                RankedAuthor::getNroOfTweets)
                .containsExactly(tuple(JOHN,
                                       2));
    }

    @Test
    public void rankSortUsersByNumberOfTweetsForAGivenTopic() throws Exception {
        //given

        rank(TOPIC,
             JOHN,
             1);
        rank(TOPIC,
             PETER,
             3);
        rank(TOPIC,
             PAUL,
             2);

        //when
        List<RankedAuthor> rank = rankingService.getRanking(TOPIC);

        //then
        assertThat(rank).isNotNull();
        assertThat(rank).extracting(RankedAuthor::getUserName,
                RankedAuthor::getNroOfTweets)
                .containsExactly(
                        tuple(PETER,
                              3),
                        tuple(PAUL,
                              2),
                        tuple(JOHN,
                              1)
                );
    }

    @Test
    public void getTopShouldLimitResultWhenTooManyElements() throws Exception {
        //given

        rank(TOPIC,
             JOHN,
             1);
        rank(TOPIC,
             PETER,
             3);
        rank(TOPIC,
             PAUL,
             2);

        //when
        List<RankedAuthor> rank = rankingService.getTop(TOPIC,
                                                      2);

        //then
        assertThat(rank).isNotNull();
        assertThat(rank).extracting(RankedAuthor::getUserName,
                RankedAuthor::getNroOfTweets)
                .containsExactly(
                        tuple(PETER,
                              3),
                        tuple(PAUL,
                              2)
                );
    }

    @Test
    public void getTopShouldHaveNoEffectWhenNotEnoughElements() throws Exception {
        //given

        rank(TOPIC,
             JOHN,
             1);

        //when
        List<RankedAuthor> rank = rankingService.getTop(TOPIC,
                                                      2);

        //then
        assertThat(rank).isNotNull();
        assertThat(rank).extracting(RankedAuthor::getUserName,
                RankedAuthor::getNroOfTweets)
                .containsExactly(
                        tuple(JOHN,
                              1)
                );
    }

    private void rank(String topic,
                      String username,
                      int times) {
        for (int i = 0; i < times; i++) {
            rankingService.rank(topic,
                                username);
        }
    }
}