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

import org.activiti.cloud.connectors.external.config.SLAProperties;
import org.springframework.stereotype.Component;

@Component
public class ShoutCallStrategySelector {

    private final SLAProperties slaProperties;

    private final SimpleShoutClient simpleClient;

    private final SLAShoutClient slaShoutClient;

    public ShoutCallStrategySelector(SLAProperties slaProperties,
                                     SimpleShoutClient simpleClient,
                                     SLAShoutClient slaShoutClient) {
        this.slaProperties = slaProperties;
        this.simpleClient = simpleClient;
        this.slaShoutClient = slaShoutClient;
    }

    public ShoutCallStrategy select() {
        return slaProperties.isEnabled()? slaShoutClient : simpleClient;
    }

}
