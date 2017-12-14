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

public class RankedUser {

    private int nroOfTweets;
    private String userName;

    public RankedUser() {
    }

    public RankedUser(String userName) {
        this.nroOfTweets = 1;
        this.userName = userName;
    }

    public int getNroOfTweets() {
        return nroOfTweets;
    }

    public void incrementTweets() {
        nroOfTweets ++;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "RankedUser{" +
                "nroOfTweets=" + nroOfTweets +
                ", userName='" + userName + '\'' +
                '}';
    }
}