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

import org.activiti.cloud.connectors.external.processor.config.SLAProperties;
import org.springframework.stereotype.Component;

@Component
public class ShoutServiceClientSelector {

    private final SLAProperties slaProperties;

    private final BasicShoutServiceClient basicShoutServiceClient;

    private final SLAShoutServiceClient slaShoutServiceClient;

    public ShoutServiceClientSelector(SLAProperties slaProperties,
                                      BasicShoutServiceClient basicShoutServiceClient,
                                      SLAShoutServiceClient slaShoutServiceClient) {
        this.slaProperties = slaProperties;
        this.basicShoutServiceClient = basicShoutServiceClient;
        this.slaShoutServiceClient = slaShoutServiceClient;
    }

    public ShoutServiceClient select() {
        return slaProperties.isEnabled()? slaShoutServiceClient : basicShoutServiceClient;
    }

}
