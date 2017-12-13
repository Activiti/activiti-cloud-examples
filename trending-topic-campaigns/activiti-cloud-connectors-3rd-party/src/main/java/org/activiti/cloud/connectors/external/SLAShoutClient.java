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

package org.activiti.cloud.connectors.external;

import org.activiti.cloud.connectors.external.model.Shout;
import org.springframework.stereotype.Component;

@Component
public class SLAShoutClient implements ShoutCallStrategy {

    private static final String SHOUT_SERVICE = "shoutApi";

    private static final int WAIT_FOR_SLA = 1000;

    private final SimpleShoutClient simpleShoutClient;

    private final RequestRateLimiterProvider requestRateLimiterProvider;

    public SLAShoutClient(SimpleShoutClient simpleShoutClient,
                          RequestRateLimiterProvider requestRateLimiterProvider) {
        this.simpleShoutClient = simpleShoutClient;
        this.requestRateLimiterProvider = requestRateLimiterProvider;
    }

    @Override
    public Shout shout(String tweet) {
        boolean serviceRequestSent = false;
        Shout shout = null;

        while (!serviceRequestSent) {
            boolean slaOverLimit = requestRateLimiterProvider.getRequestRateLimiter().overLimitWhenIncremented(SHOUT_SERVICE);
            if (!slaOverLimit) {

                shout = simpleShoutClient.shout(tweet);
                serviceRequestSent = true;
            } else {
                System.out.println(">> Waiting for SLAs allowance ...");
                try {
                    Thread.sleep(WAIT_FOR_SLA);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return shout;
    }

}
