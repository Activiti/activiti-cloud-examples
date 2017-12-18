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

package org.activiti.cloud.connectors.external.processor;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.activiti.cloud.connectors.external.processor.config.SLAProperties;
import org.springframework.stereotype.Component;

@Component
public class RequestRateLimiterProvider {

    private final SLAProperties slaProperties;

    private RequestRateLimiter requestRateLimiter;

    public RequestRateLimiterProvider(SLAProperties slaProperties) {
        this.slaProperties = slaProperties;
    }

    public RequestRateLimiter getRequestRateLimiter(){
        if (requestRateLimiter == null) {
            Set<RequestLimitRule> rules = Collections.singleton(RequestLimitRule.of(1,
                                                                                    TimeUnit.MINUTES,
                                                                                    slaProperties.getRequests())); // request per minute, per key
            requestRateLimiter = new InMemorySlidingWindowRequestRateLimiter(rules);
            System.out.println("Rate limit - postconstruct - rpm " + slaProperties.getRequests());
        }
        return requestRateLimiter;
    }

}
