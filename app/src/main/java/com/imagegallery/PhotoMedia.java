package com.imagegallery;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
class PhotoMedia implements ImageUrl {

    @SerializedName("m")
    private String url;

    public String getUrl() {
        return url;
    }
}
