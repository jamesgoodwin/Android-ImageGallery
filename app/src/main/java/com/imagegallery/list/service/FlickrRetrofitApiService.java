package com.imagegallery.list.service;

import com.imagegallery.model.PhotoSearchResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

interface FlickrRetrofitApiService {

    String SEARCH_PATH = "/services/feeds/photos_public.gne?format=json&nojsoncallback=true";

    @GET(SEARCH_PATH)
    Single<PhotoSearchResult> listPhotos();

    @GET(SEARCH_PATH)
    Single<PhotoSearchResult> searchPhotos(@Query("tags") String tags);

}