package com.dappsindia.telegrammoviebot;

import java.util.List;

public class MovieModel {

    private String movieTitle;
    private String movieThumbnail;
    private String channelLink;
    private Long channelId;
    private Long channelAccessHash;
    private List<MovieLinksModel> movieLinks;

    private String key;

    public MovieModel() {
    }

    public MovieModel(String movieTitle, String movieThumbnail, String channelLink, Long channelId, Long channelAccessHash, List<MovieLinksModel> movieLinks) {
        this.movieTitle = movieTitle;
        this.movieThumbnail = movieThumbnail;
        this.channelLink = channelLink;
        this.channelId = channelId;
        this.channelAccessHash = channelAccessHash;
        this.movieLinks = movieLinks;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieThumbnail() {
        return movieThumbnail;
    }

    public void setMovieThumbnail(String movieThumbnail) {
        this.movieThumbnail = movieThumbnail;
    }

    public List<MovieLinksModel> getMovieLinks() {
        return movieLinks;
    }

    public void setMovieLinks(List<MovieLinksModel> movieLinks) {
        this.movieLinks = movieLinks;
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

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getChannelAccessHash() {
        return channelAccessHash;
    }

    public void setChannelAccessHash(Long channelAccessHash) {
        this.channelAccessHash = channelAccessHash;
    }
}
