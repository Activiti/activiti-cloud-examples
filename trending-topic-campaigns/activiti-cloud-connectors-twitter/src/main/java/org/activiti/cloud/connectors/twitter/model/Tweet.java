package org.activiti.cloud.connectors.twitter.model;

public class Tweet {

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

    @Override
    public String toString() {
        return "Tweet{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}
