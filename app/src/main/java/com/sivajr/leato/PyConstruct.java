package com.sivajr.leato;

public class PyConstruct {

    private String title;
    private String details;
    private String period;
    private String key;

    public PyConstruct(){

    }

    public PyConstruct(String title, String details, String period) {

        this.title = title;
        this.details = details;
        this.period = period;

    }

    public String getTitle() {

        return title;

    }

    public void setTitle(String title) {

        this.title = title;

    }

    public String getDetails() {

        return details;

    }

    public void setDetails(String details) {

        this.details = details;

    }

    public String getPeriod() {

        return period;

    }

    public void setPeriod(String period) {

        this.period = period;

    }

    public String getKey() {

        return key;

    }

    public void setKey(String key) {

        this.key = key;

    }

}