package com.imagegallery.list.service;

import com.imagegallery.model.PhotoSearchResult;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;

public class FlickrImageService implements ImageService {

    private static final String FLICKR_BASE_URL = "https://api.flickr.com";

    private final FlickrRetrofitApiService apiService;

    public FlickrImageService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(FLICKR_BASE_URL)
                .client(new OkHttpClient())
                .build();

        apiService = retrofit.create(FlickrRetrofitApiService.class);
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
