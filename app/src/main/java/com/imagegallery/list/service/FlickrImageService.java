package com.imagegallery.list.service;

import com.imagegallery.model.PhotoSearchResult;
import com.imagegallery.model.PhotoSearchResultItem;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlickrImageService implements ImageService {

    private static final String FLICKR_BASE_URL = "https://api.flickr.com";

    private final FlickrRetrofitApiService apiService;

    public FlickrImageService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(FLICKR_BASE_URL)
                .client(new OkHttpClient())
                .build();

        apiService = retrofit.create(FlickrRetrofitApiService.class);
    }

    @Override
    public Single<PhotoSearchResult> listPhotos() {
        return listPhotos(DATE_TAKEN_DESC_SORT_TYPE);
    }

    @Override
    public Single<PhotoSearchResult> listPhotos(int sortType) {
        return apiService.listPhotos()
                .flatMap(sortResults(sortType));
    }

    @Override
    public Single<PhotoSearchResult> searchPhotos(String query) {
        return searchPhotos(query, DATE_TAKEN_DESC_SORT_TYPE);
    }

    @Override
    public Single<PhotoSearchResult> searchPhotos(String query, int sortType) {
        String formattedQuery = query.replaceAll("/[,\\s]/", ",");

        return apiService.searchPhotos(formattedQuery)
                .flatMap(sortResults(sortType));
    }

    private Function<PhotoSearchResult, SingleSource<PhotoSearchResult>> sortResults(int sortType) {
        return photoSearchResult -> {
            List<PhotoSearchResultItem> searchResults = photoSearchResult.getItems();

            switch (sortType) {
                case DATE_PUBLISHED_DESC_SORT_TYPE:
                    sortByDatePublished(searchResults);
                    break;
                default:
                    sortByDateTaken(searchResults);
            }

            return Single.just(photoSearchResult);
        };
    }

    private void sortByDatePublished(List<PhotoSearchResultItem> searchResults) {
        Collections.sort(searchResults, (result1, result2)
                -> result1.getDatePublished().compareTo(result2.getDatePublished()));
    }

    private void sortByDateTaken(List<PhotoSearchResultItem> searchResults) {
        Collections.sort(searchResults, (result1, result2)
                -> result1.getDateTaken().compareTo(result2.getDateTaken()));
    }
}