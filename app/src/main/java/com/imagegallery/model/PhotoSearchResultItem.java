package com.imagegallery.model;

import com.google.gson.annotations.SerializedName;
import com.imagegallery.list.ImageSearchResult;

import java.util.Date;

@SuppressWarnings("unused")
class PhotoSearchResultItem implements ImageSearchResult {

    private String title;

    private String link;

    private PhotoMedia media;

    @SerializedName("date_taken")
    private Date dateTaken;

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

    @Override
    public String getUrl() {
        return media.getUrl();
    }

    @Override
    public String getDescription() {
        return title;
    }
}
