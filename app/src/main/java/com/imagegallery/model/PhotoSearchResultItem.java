package com.imagegallery.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@SuppressWarnings("unused")
public class PhotoSearchResultItem {

    private String title;

    private String link;

    private PhotoMedia media;

    @SerializedName("date_taken")
    private Date dateTaken;

    @SerializedName("published")
    private Date datePublished;

    public PhotoSearchResultItem(String title, String link, PhotoMedia media, Date dateTaken, Date datePublished) {
        this.title = title;
        this.link = link;
        this.media = media;
        this.dateTaken = dateTaken;
        this.datePublished = datePublished;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public PhotoMedia getMedia() {
        return media;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public String getUrl() {
        return media.getUrl();
    }

    @Override
    public String toString() {
        return "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", media=" + media +
                ", dateTaken=" + dateTaken +
                ", datePublished=" + datePublished;
    }
}
