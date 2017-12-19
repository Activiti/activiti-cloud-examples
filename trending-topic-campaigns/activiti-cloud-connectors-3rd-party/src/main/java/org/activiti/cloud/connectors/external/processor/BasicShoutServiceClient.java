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

import org.activiti.cloud.connectors.external.processor.model.Shout;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BasicShoutServiceClient implements ShoutServiceClient {

    private static final String SHOUT_SERVICE_URL = "http://API.SHOUTCLOUD.IO/V1/SHOUT";

    private final RestTemplate restTemplate;

    public BasicShoutServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Shout shout(String tweet) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Shout> request = new HttpEntity<>(new Shout(tweet),
                                                     headers);
        ResponseEntity<Shout> response = restTemplate
                .exchange(SHOUT_SERVICE_URL,
                          HttpMethod.POST,
                          request,
                          Shout.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Error Calling my shouty service for tweet: " + tweet);
        }

        return response.getBody();
    }

}
