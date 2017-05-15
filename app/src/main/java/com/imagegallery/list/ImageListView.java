package com.imagegallery.list;

import com.imagegallery.model.PhotoSearchResultItem;

import java.util.List;

interface ImageListView {

    void showImages(List<PhotoSearchResultItem> images);

    void showLoading(boolean show);

    void showConnectionError();

}