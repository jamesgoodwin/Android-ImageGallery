package com.imagegallery.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
class PhotoMedia {

    @SerializedName("m")
    private String url;

    public PhotoMedia(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
