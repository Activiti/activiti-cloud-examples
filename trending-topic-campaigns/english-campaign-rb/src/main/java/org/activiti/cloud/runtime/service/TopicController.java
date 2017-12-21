package org.activiti.cloud.runtime.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static net.logstash.logback.marker.Markers.append;

@RestController
public class TopicController {

    private Logger logger = LoggerFactory.getLogger(TopicController.class);
    @Value("spring.application.name")
    private String appName;

    private final String currentTopic;

    public TopicController( @Value("${campaign.topic}") String currentTopic) {
        this.currentTopic = currentTopic;
        logger.debug("TopicController Created for topic: " + this.currentTopic);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/topic")
    public String getCurrentTopic() {
        return currentTopic;
    }

    public boolean matchTopic(String text, String author) {
        boolean match = text.toLowerCase().contains(currentTopic.toLowerCase());
        logger.info(append("service-name", appName), (match?"Match ":"No Match")+" to '"+currentTopic+"' on Tweet by "+author);
        return match;
    }
}

