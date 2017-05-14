package com.imagegallery;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;

public class FlickrImageService implements ImageService {

    private final FlickrRetrofitApiService apiService;

    public FlickrImageService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://api.flickr.com")
                .client(new OkHttpClient())
                .build();

        apiService = retrofit.create(FlickrRetrofitApiService.class);
    }

    @Override
    public Single<PhotoSearchResult> listPhotos() {
        return apiService.listPhotos();
    }

    @Override
    public Single<PhotoSearchResult> searchPhotos(List<String> searchTerms) {
        return null;
    }
}
