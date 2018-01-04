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

import org.activiti.cloud.connectors.external.analyzer.NLP;
import org.activiti.cloud.connectors.starter.configuration.EnableActivitiCloudConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import static net.logstash.logback.marker.Markers.append;

@SpringBootApplication
@EnableActivitiCloudConnector
@ComponentScan({"org.activiti.cloud.connectors.starter", "org.activiti.cloud.connectors.external"})
public class ExternalServiceCloudConnector implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(ExternalServiceCloudConnector.class);
    @Value("${spring.application.name}")
    private String appName;

    public ExternalServiceCloudConnector() {
    }

    public static void main(String[] args) {
        SpringApplication.run(ExternalServiceCloudConnector.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.debug(append("service-name", appName),"Starting Analyzer Connector Application");
        NLP.init();
    }
}
