package com.dappsindia.telegrammoviebot;

public class ChannelModel {
    private Long channelId;
    private Long channelAccessHash;
    private String channelTitle;
    private String channelLink;

    private String key;

    public ChannelModel() {
    }

    public ChannelModel(Long channelId, Long channelAccessHash, String channelTitle, String channelLink) {
        this.channelId = channelId;
        this.channelAccessHash = channelAccessHash;
        this.channelTitle = channelTitle;
        this.channelLink = channelLink;
    }

    public String getChannelLink() {
        return channelLink;
    }

    public void setChannelLink(String channelLink) {
        this.channelLink = channelLink;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getChannelAccessHash() {
        return channelAccessHash;
    }

    public void setChannelAccessHash(Long channelAccessHash) {
        this.channelAccessHash = channelAccessHash;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }
}
