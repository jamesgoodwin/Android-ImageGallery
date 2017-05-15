package com.imagegallery.list;

import android.util.Log;

import com.imagegallery.list.service.ImageService;
import com.imagegallery.model.PhotoSearchResult;
import com.imagegallery.model.PhotoSearchResultItem;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

class ImageListPresenter {

    public static final int DATE_TAKEN_DESC_SORT_TYPE = 0;
    public static final int DATE_PUBLISHED_DESC_SORT_TYPE = 1;

    private ImageListView view;
    private final ImageService imageService;

    private Scheduler viewScheduler;
    private Scheduler backgroundScheduler;

    private PhotoSearchResult currentSearchResults;

    ImageListPresenter(ImageListView view, ImageService imageService, Scheduler viewScheduler, Scheduler backgroundScheduler) {
        this.view = view;
        this.imageService = imageService;
        this.viewScheduler = viewScheduler;
        this.backgroundScheduler = backgroundScheduler;
    }

    void requestImages() {
        view.showLoading(true);

        imageService.listPhotos()
                .subscribeOn(backgroundScheduler)
                .observeOn(viewScheduler)
                .doAfterTerminate(hideLoading())
                .subscribe(onSuccess(), onError());
    }

    void requestImages(String query) {
        view.showLoading(true);

        imageService.searchPhotos(query)
                .subscribeOn(backgroundScheduler)
                .observeOn(viewScheduler)
                .doAfterTerminate(hideLoading())
                .subscribe(onSuccess(), onError());
    }

    private Consumer<PhotoSearchResult> onSuccess() {
        return photoSearchResult -> {
            currentSearchResults = photoSearchResult;
            view.showImages(photoSearchResult.getItems());
        };
    }

    private Consumer<Throwable> onError() {
        return throwable -> {
            Log.e("ImageGallery", "Error retrieving images", throwable);

            if(throwable instanceof UnknownHostException) {
                view.showConnectionError();
            } else {
                view.showGenericError();
            }
        };
    }

    private Action hideLoading() {
        return () -> view.showLoading(false);
    }

    void onDestroy() {
        this.view = null;
    }

    void showSortedSearchResults(int sortType) {
        if (currentSearchResults != null) {
            List<PhotoSearchResultItem> searchResultItems = currentSearchResults.getItems();

            switch (sortType) {
                case DATE_TAKEN_DESC_SORT_TYPE:
                    sortByDateTaken(searchResultItems);
                    break;
                default:
                    sortByDatePublished(searchResultItems);
            }

            view.showImages(searchResultItems);
        }
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