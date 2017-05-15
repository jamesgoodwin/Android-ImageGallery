package com.imagegallery.list;

import com.imagegallery.list.service.ImageService;
import com.imagegallery.model.PhotoSearchResult;

import java.net.UnknownHostException;

import io.reactivex.Scheduler;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

class ImageListPresenter {

    private ImageListView view;
    private final ImageService imageService;

    private Scheduler viewScheduler;
    private Scheduler backgroundScheduler;

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
        return photoSearchResult -> view.showImages(photoSearchResult.getItems());
    }

    private Consumer<Throwable> onError() {
        return throwable -> {
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
}