package org.activiti.cloud.runtime.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static net.logstash.logback.marker.Markers.append;

@Component("sendRewardDelegate")
public class SendRewardDelegate implements JavaDelegate {

    private Logger logger = LoggerFactory.getLogger(SendRewardDelegate.class);
    @Value("${spring.application.name}")
    private String appName;


    @Override
    public void execute(DelegateExecution delegateExecution) {
        logger.info(append("service-name", appName),"#################################################################################");
        logger.info(append("service-name", appName),"#  Reward time!!! You WON!!! ");
        logger.info(append("service-name", appName)," I'm tweeting to a Winner: " + delegateExecution.getVariables().get("winner") + " \n");
        logger.info(append("service-name", appName),"#################################################################################");

    }
}
