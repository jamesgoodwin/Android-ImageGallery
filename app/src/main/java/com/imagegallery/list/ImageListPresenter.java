package com.imagegallery.list;

import com.imagegallery.list.service.ImageService;
import com.imagegallery.model.PhotoSearchResult;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

class ImageListPresenter {

    private ImageListView view;
    private ImageService imageService;

    ImageListPresenter(ImageListView view, ImageService imageService) {
        this.view = view;
        this.imageService = imageService;
    }

    void requestImages() {
        view.showLoading(true);

        imageService.listPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        view.showLoading(false);
                    }
                })
                .subscribe(new Action1<PhotoSearchResult>() {
                    @Override
                    public void call(PhotoSearchResult photoSearchResult) {
                        view.showImages(photoSearchResult.getItems());
                    }
                });
    }

    void requestImages(String query) {
        view.showLoading(true);

        imageService.searchPhotos(query)
                .subscribeOn(Schedulers.io())
                .observeOn(mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        view.showLoading(false);
                    }
                })
                .subscribe(new Action1<PhotoSearchResult>() {
                    @Override
                    public void call(PhotoSearchResult photoSearchResult) {
                        view.showImages(photoSearchResult.getItems());
                    }
                });
    }

    void onDestroy() {
        this.view = null;
    }
}