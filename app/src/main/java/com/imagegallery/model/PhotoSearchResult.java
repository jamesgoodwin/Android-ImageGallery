package com.imagegallery.model;

import com.imagegallery.list.ImageSearchResult;

import java.util.List;

@SuppressWarnings("unused")
public class PhotoSearchResult {

    private String title;
    private List<PhotoSearchResultItem> items;

    public String getTitle() {
        return title;
    }

    public List<? extends ImageSearchResult> getItems() {
        return items;
    }

}
