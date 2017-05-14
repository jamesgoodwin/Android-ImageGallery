package com.imagegallery;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

class PhotoSearchResultItem implements ImageSearchResult {

    private String title;

    private String link;

    private PhotoMedia media;

    @SerializedName("date_taken")
    private Date dateTaken;

}
