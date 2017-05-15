package com.imagegallery.list.service;

import com.imagegallery.model.PhotoSearchResult;

import io.reactivex.Single;

public interface ImageService {

    Single<PhotoSearchResult> listPhotos();

    Single<PhotoSearchResult> searchPhotos(String query);

}