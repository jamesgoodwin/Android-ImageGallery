package com.imagegallery.list.service;

import com.imagegallery.model.PhotoSearchResult;

import io.reactivex.Single;

public interface ImageService {

    int DATE_TAKEN_DESC_SORT_TYPE = 0;
    int DATE_PUBLISHED_DESC_SORT_TYPE = 1;

    Single<PhotoSearchResult> listPhotos();

    Single<PhotoSearchResult> listPhotos(int sortType);

    Single<PhotoSearchResult> searchPhotos(String query);

    Single<PhotoSearchResult> searchPhotos(String query, int sortType);

}