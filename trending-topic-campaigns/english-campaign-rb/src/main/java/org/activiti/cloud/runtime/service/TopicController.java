package org.activiti.cloud.runtime.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicController {

    @Value("${campaign.topic}")
    private String currentTopic;

    @RequestMapping(method = RequestMethod.GET, path = "/topic")
    public String getCurrentTopic() {
        return currentTopic;
    }

    public TopicController() {
        System.out.println("TopicController Created for topic: " + currentTopic);
    }

    public boolean matchTopic(String text) {
        return text.toLowerCase().contains(currentTopic.toLowerCase());
    }
}

