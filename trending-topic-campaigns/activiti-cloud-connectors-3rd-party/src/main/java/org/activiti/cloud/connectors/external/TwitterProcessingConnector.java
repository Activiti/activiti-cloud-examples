package org.activiti.cloud.connectors.external;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.activiti.cloud.connectors.external.model.Shout;
import org.activiti.cloud.connectors.starter.channels.CloudConnectorChannels;
import org.activiti.cloud.connectors.starter.model.IntegrationRequestEvent;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${sla.requests}")
    private int requestPerMinute;

    @Value("${sla.enabled}")
    private boolean slaEnabled;

    private static final int WAIT_FOR_SLA = 1000;

    private static final String SHOUT_SERVICE = "shoutApi";

    private static final String SHOUT_SERVICE_URL = "http://API.SHOUTCLOUD.IO/V1/SHOUT";

    private RequestRateLimiter requestRateLimiter;

    public TwitterProcessingConnector() {

        Set<RequestLimitRule> rules = Collections.singleton(RequestLimitRule.of(1,
                                                                                TimeUnit.MINUTES,
                                                                                requestPerMinute)); // request per minute, per key
        requestRateLimiter = new InMemorySlidingWindowRequestRateLimiter(rules);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    @StreamListener(value = CloudConnectorChannels.INTEGRATION_EVENT_CONSUMER, condition = "headers['connectorType']=='Process English Tweet'")
    public synchronized void processEnglish(IntegrationRequestEvent event) throws InterruptedException {

        String tweet = String.valueOf(event.getVariables().get("text"));

        if (slaEnabled) {
            slaEnabledRequestHandling(tweet,
                                      event);
        } else {
            peformRequest(tweet,
                          event);
        }
    }

    private void peformRequest(String tweet,
                               IntegrationRequestEvent event) {
        Shout shout = executeRequestToShoutService(tweet);

        completeIntegrationRequest(event,
                                   shout);
    }

    private void slaEnabledRequestHandling(String tweet,
                                           IntegrationRequestEvent event) {
        boolean serviceRequestSent = false;

        while (!serviceRequestSent) {
            boolean slaOverLimit = requestRateLimiter.overLimitWhenIncremented(SHOUT_SERVICE);
            if (!slaOverLimit) {

                peformRequest(tweet,
                              event);
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
    }

    private Shout executeRequestToShoutService(String tweet) {
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

    private void completeIntegrationRequest(IntegrationRequestEvent event,
                                            Shout shout) {
        Map<String, Object> results = new HashMap<>();

        results.put("text",
                    shout.getOUTPUT());

        IntegrationResultEvent ire = new IntegrationResultEvent(event.getExecutionId(),
                                                                results);

        integrationResultsProducer.send(MessageBuilder.withPayload(ire).build());
    }
}
