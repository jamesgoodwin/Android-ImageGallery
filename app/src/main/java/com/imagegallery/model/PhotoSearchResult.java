package com.imagegallery.model;

import com.imagegallery.list.ImageSearchResult;

import java.util.List;

@SuppressWarnings("unused")
public class PhotoSearchResult {

    private String title;
    private List<PhotoSearchResultItem> items;

    public PhotoSearchResult(String title, List<PhotoSearchResultItem> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public List<? extends ImageSearchResult> getItems() {
        return items;
    }

}
