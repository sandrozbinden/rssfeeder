package com.sandrozbinden.rss;

import java.util.Date;

public class Feed {

    private String id;
    private String title;
    private String text;
    private String language;
    private String newspaper;
    private String link;
    private Date publishingDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

    public void setNewspaper(String newspaper) {
        this.newspaper = newspaper;
    }

    public String getNewspaper() {
        return newspaper;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
