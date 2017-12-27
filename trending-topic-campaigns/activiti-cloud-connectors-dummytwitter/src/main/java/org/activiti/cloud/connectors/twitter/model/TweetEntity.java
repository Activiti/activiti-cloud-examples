package org.activiti.cloud.connectors.twitter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name="tweet")
public class TweetEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String lang;


    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public String getLang() {
        return lang;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
