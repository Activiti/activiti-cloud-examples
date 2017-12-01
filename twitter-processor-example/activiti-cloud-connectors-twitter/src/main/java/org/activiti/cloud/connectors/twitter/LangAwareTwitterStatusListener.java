package org.activiti.cloud.connectors.twitter;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.services.api.commands.StartProcessInstanceCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

@Component
public class LangAwareTwitterStatusListener implements StatusListener {

    @Autowired
    private LanguageMappingsController languageMappingsController;

    @Autowired
    private MessageChannel runtimeCmdProducer;


    public void onStatus(Status status) {
        //Start a process
        Map<String, Object> vars = new HashMap<>();
        String lang = status.getLang();
        vars.put("message",
                 status.getText());
        vars.put("lang",
                 lang);
        vars.put("user",
                 status.getUser().getName());
        vars.put("location",
                 status.getUser().getLocation());

        if (languageMappingsController.getLanguageMapping(lang) != null) {
            requestStartNewProcessInstanceForTweet(vars,
                                                   lang);
        } else {
            // Log no process defined for language lang
        }
    }

    private void requestStartNewProcessInstanceForTweet(Map<String, Object> vars,
                                                        String lang) {
        StartProcessInstanceCmd startProcessInstanceCmd = new StartProcessInstanceCmd(languageMappingsController.getLanguageMapping(lang),
                                                                                      vars);
        runtimeCmdProducer.send(MessageBuilder.withPayload(startProcessInstanceCmd).build());
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
    }

    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
    }

    @Override
    public void onScrubGeo(long l,
                           long l1) {
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
    }

    public void onException(Exception ex) {
        ex.printStackTrace();
    }
}
