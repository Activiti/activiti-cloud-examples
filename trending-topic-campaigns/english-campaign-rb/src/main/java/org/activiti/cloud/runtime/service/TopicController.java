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

    @RequestMapping(method = RequestMethod.POST, path = "/topic")
    public void setCurrentTopic(String filter) {
        currentTopic = filter;
    }

    public TopicController() {
        System.out.println("TopicController Created for topic: " + currentTopic);
    }

    public boolean matchTopic(String text) {
        System.out.println("Matching " + text + "with " + currentTopic);
        boolean match = text.toLowerCase().contains(currentTopic.toLowerCase());
        if (match) {
            System.out.println("new " + currentTopic.toLowerCase() + " tweet found!");
        } else {
            System.out.println("NO MATCH");
        }
        return match;
    }
}

