package org.activiti.cloud.runtime.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterController {

    public static String currentFilter = "TRUMP";

    @RequestMapping(method = RequestMethod.GET, path = "/filter")
    public String getCurrentFilter() {
        return currentFilter;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/filter")
    public void setCurrentFilter(String filter) {
        currentFilter = filter;
    }

    public static boolean matchFilter(String text) {
        System.out.println("Applying Current Filter: " + currentFilter + " to -> " + text);
        return text.toLowerCase().contains(currentFilter.toLowerCase());
    }
}

