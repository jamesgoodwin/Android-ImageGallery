package com.imagegallery.list;

import com.imagegallery.list.service.ImageService;
import com.imagegallery.model.PhotoSearchResult;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
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
                .subscribe(new Consumer<PhotoSearchResult>() {
                    @Override
                    public void accept(@NonNull PhotoSearchResult photoSearchResult) throws Exception {
                        view.showImages(photoSearchResult.getItems());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.showConnectionError();
                    }
                });
    }

    void requestImages(String query) {
        view.showLoading(true);

        imageService.searchPhotos(query)
                .subscribeOn(backgroundScheduler)
                .observeOn(viewScheduler)
                .doAfterTerminate(hideLoading())
                .subscribe(new Consumer<PhotoSearchResult>() {
                    @Override
                    public void accept(@NonNull PhotoSearchResult photoSearchResult) throws Exception {
                        view.showImages(photoSearchResult.getItems());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.showConnectionError();
                    }
                });
    }

    @SuppressWarnings("Convert2Lambda")
    private Action hideLoading() {
        return new Action() {
            @Override
            public void run() throws Exception {
                view.showLoading(false);
            }
        };
    }

    void onDestroy() {
        this.view = null;
    }
}