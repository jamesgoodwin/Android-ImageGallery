package com.imagegallery.list;

import com.imagegallery.list.service.ImageService;

import io.reactivex.Scheduler;
import io.reactivex.functions.Action;

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
                .doAfterTerminate(() -> view.showLoading(false))
                .subscribe(photoSearchResult -> view.showImages(photoSearchResult.getItems()));
    }

    void requestImages(String query) {
        view.showLoading(true);

        imageService.searchPhotos(query)
                .subscribeOn(backgroundScheduler)
                .observeOn(viewScheduler)
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.showLoading(false);
                    }
                })
                .subscribe(photoSearchResult -> view.showImages(photoSearchResult.getItems()));
    }

    void onDestroy() {
        this.view = null;
    }
}