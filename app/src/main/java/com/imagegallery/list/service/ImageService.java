package com.imagegallery.list.service;

import com.imagegallery.model.PhotoSearchResult;

import rx.Single;

public interface ImageService {

    Single<PhotoSearchResult> listPhotos();

    Single<PhotoSearchResult> searchPhotos(String query);

}