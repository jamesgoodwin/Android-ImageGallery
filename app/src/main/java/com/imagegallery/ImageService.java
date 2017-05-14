package com.imagegallery;

import java.util.List;

import rx.Single;

public interface ImageService {

    Single<PhotoSearchResult> listPhotos();

    Single<PhotoSearchResult> searchPhotos(List<String> searchTerms);

}