package com.imagegallery.list.service;

import com.imagegallery.model.PhotoSearchResult;
import com.imagegallery.model.PhotoSearchResultItem;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class FlickrImageService implements ImageService {

    public static final String FLICKR_BASE_URL = "https://api.flickr.com";

    private final FlickrRetrofitApiService apiService;

    public FlickrImageService(FlickrRetrofitApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Single<PhotoSearchResult> listPhotos() {
        return listPhotos(DATE_PUBLISHED_DESC_SORT_TYPE);
    }

    @Override
    public Single<PhotoSearchResult> listPhotos(int sortType) {
        return apiService.listPhotos()
                .flatMap(sortResults(sortType));
    }

    @Override
    public Single<PhotoSearchResult> searchPhotos(String query) {
        return searchPhotos(query, DATE_PUBLISHED_DESC_SORT_TYPE);
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
                case DATE_TAKEN_DESC_SORT_TYPE:
                    sortByDateTaken(searchResults);
                    break;
                default:
                    sortByDatePublished(searchResults);
            }

            return Single.just(photoSearchResult);
        };
    }

    private void sortByDatePublished(List<PhotoSearchResultItem> searchResults) {
        Collections.sort(searchResults, (result1, result2)
                -> result2.getDatePublished().compareTo(result1.getDatePublished()));
    }

    private void sortByDateTaken(List<PhotoSearchResultItem> searchResults) {
        Collections.sort(searchResults, (result1, result2)
                -> result2.getDateTaken().compareTo(result1.getDateTaken()));
    }
}