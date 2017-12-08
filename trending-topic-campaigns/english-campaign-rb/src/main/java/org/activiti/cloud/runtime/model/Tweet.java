package org.activiti.cloud.runtime.model;

import java.io.Serializable;

public class Tweet implements Serializable {

    private String text;
    private String author;
    private String lang;

    public Tweet() {
    }

    public Tweet(String text,
                 String author,
                 String lang) {
        this.text = text;
        this.author = author;
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public String getLang() {
        return lang;
    }
}
