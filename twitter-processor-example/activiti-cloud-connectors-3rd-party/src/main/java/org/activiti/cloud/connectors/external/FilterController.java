package org.activiti.cloud.connectors.external;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterController {

    public static String currentFilter = "OXFORD";

    @RequestMapping(method = RequestMethod.GET, path = "/filter")
    public String getCurrentFilter() {
        return currentFilter;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/filter")
    public void setCurrentFilter(String filter) {
        currentFilter = filter;
    }
}

