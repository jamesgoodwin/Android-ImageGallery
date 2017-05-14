package com.imagegallery.list;

import java.util.List;

interface ImageListView {

    void showImages(List<? extends ImageSearchResult> images);

    void showLoading(boolean show);

}