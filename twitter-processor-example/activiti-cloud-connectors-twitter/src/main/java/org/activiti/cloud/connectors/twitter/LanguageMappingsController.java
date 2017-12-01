package org.activiti.cloud.connectors.twitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LanguageMappingsController {

    // Language, Process Definition
    private Map<String, String> languageMappings = new ConcurrentHashMap<>();

    @RequestMapping(method = RequestMethod.GET, path = "/lang-mappings/{lang}")
    public String getLanguageMapping(@PathParam("lang") String lang) {
        return languageMappings.get(lang);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/lang-mappings/{lang}")
    public void setLanguageMapping(@PathParam("lang") String lang,
                                   @RequestBody String processDefId) {
        languageMappings.put(lang,
                             processDefId);
    }

    public Map<String, String> getLanguageMappings() {
        return languageMappings;
    }
}

