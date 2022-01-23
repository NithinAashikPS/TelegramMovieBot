package com.dappsindia.telegrammoviebot;

public class MovieLinksModel {

    private String linkKey;
    private String linkValue;

    public MovieLinksModel() {
    }

    public MovieLinksModel(String linkKey, String linkValue) {
        this.linkKey = linkKey;
        this.linkValue = linkValue;
    }

    public String getLinkKey() {
        return linkKey;
    }

    public void setLinkKey(String linkKey) {
        this.linkKey = linkKey;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public void setLinkValue(String linkValue) {
        this.linkValue = linkValue;
    }
}
