package org.activiti.cloud.connectors.external;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.activiti.cloud.connectors.external.model.Shout;
import org.activiti.cloud.connectors.starter.channels.CloudConnectorChannels;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TwitterProcessingConnector {

    @Autowired
    private MessageChannel integrationResultsProducer;

    @Autowired
    private RestTemplate restTemplate;

    RequestRateLimiter requestRateLimiter;

    public TwitterProcessingConnector() {

        Set<RequestLimitRule> rules = Collections.singleton(RequestLimitRule.of(1,
                                                                                TimeUnit.MINUTES,
                                                                                60)); // 50 request per minute, per key
        requestRateLimiter = new InMemorySlidingWindowRequestRateLimiter(rules);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Process English Tweet'")
    public synchronized void processEnglish(IntegrationRequestEvent event) throws InterruptedException {

        // System.out.println("Just recieved an integration request event: " + event);

        String message = String.valueOf(event.getVariables().get("message"));

        boolean sent = false;
        while (!sent) {
            boolean overLimit = requestRateLimiter.overLimitWhenIncremented("shoutApi");
            System.out.println(">>>>> !!!!! Over Limit --> ??" + overLimit);
            if (!overLimit) {

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Shout> request = new HttpEntity<>(new Shout(message),
                                                             headers);
                ResponseEntity<Shout> response = restTemplate
                        .exchange("http://API.SHOUTCLOUD.IO/V1/SHOUT",
                                  HttpMethod.POST,
                                  request,
                                  Shout.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                    System.out.println(">>> XXXXX Error Calling my shouty service for; " + message);
                    return;
                }

                Shout shout = response.getBody();

                System.out.println("Shout: " + shout);
                Map<String, Object> results = new HashMap<>();

                results.put("shout",
                            shout.getOUTPUT());

                IntegrationResultEvent ire = new IntegrationResultEvent(UUID.randomUUID().toString(),
                                                                        event.getExecutionId(),
                                                                        results);

                //  System.out.println("I'm sending back an integratrion Result: " + ire);
                integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
                sent = true;
            } else {
                System.out.println(">> Waiting for SLAs allowance ...");
                Thread.sleep(1000);
            }
        }
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Filter English Tweet'")
    public synchronized void filterEnglish(IntegrationRequestEvent event) throws InterruptedException {

        // System.out.println("Just received an integration request event: " + event);

        String message = String.valueOf(event.getVariables().get("message"));

        Map<String, Object> results = new HashMap<>();
        results.put("filterApplied",
                    FilterController.currentFilter);
        if (message.toLowerCase().contains(FilterController.currentFilter.toLowerCase())) {
            System.out.println(" >>> YYYY : Tweet was approved to be ranked: " + message);
            results.put("approved",
                        true);
        } else {
            //System.out.println(" >>> NNNN: Tweet was not approved to be ranked!");
            results.put("approved",
                        false);
        }

        IntegrationResultEvent ire = new IntegrationResultEvent(UUID.randomUUID().toString(),
                                                                event.getExecutionId(),
                                                                results);
        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }
}
