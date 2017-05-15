package com.imagegallery.list.service;

import com.imagegallery.model.PhotoSearchResult;

import io.reactivex.Single;

public class FlickrImageService implements ImageService {

    public static final String FLICKR_BASE_URL = "https://api.flickr.com";

    private final FlickrRetrofitApiService apiService;

    public FlickrImageService(FlickrRetrofitApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Single<PhotoSearchResult> listPhotos() {
        return apiService.listPhotos();
    }

    @Override
    public Single<PhotoSearchResult> searchPhotos(String query) {
        String formattedQuery = query.replaceAll("/[,\\s]/", ",");

        return apiService.searchPhotos(formattedQuery);
    }

}