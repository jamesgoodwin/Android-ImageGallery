package com.imagegallery;

import java.util.List;

@SuppressWarnings("unused")
public class PhotoSearchResult {

    private String title;
    private List<PhotoSearchResultItem> items;

    public String getTitle() {
        return title;
    }

    public List<PhotoSearchResultItem> getItems() {
        return items;
    }

}
