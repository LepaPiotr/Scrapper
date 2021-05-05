package com.MongoApp.app.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Search {
    @Id
    private String id;
    private String phrase;
    private Date searchDate;

    public Search() {
    }

    public Search(String phrase, Date searchDate) {
        this.phrase = phrase;
        this.searchDate = searchDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }
}
