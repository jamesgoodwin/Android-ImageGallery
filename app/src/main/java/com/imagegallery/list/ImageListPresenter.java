package com.imagegallery.list;

import com.imagegallery.list.service.ImageService;
import com.imagegallery.model.PhotoSearchResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.showLoading(false);
                    }
                })
                .subscribe(new Consumer<PhotoSearchResult>() {
                    @Override
                    public void accept(@NonNull PhotoSearchResult photoSearchResult) throws Exception {
                        view.showImages(photoSearchResult.getItems());
                    }
                });
    }

    void requestImages(String query) {
        view.showLoading(true);

        imageService.searchPhotos(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.showLoading(false);
                    }
                })
                .subscribe(new Consumer<PhotoSearchResult>() {
                    @Override
                    public void accept(@NonNull PhotoSearchResult photoSearchResult) throws Exception {
                        view.showImages(photoSearchResult.getItems());
                    }
                });
    }

    void onDestroy() {
        this.view = null;
    }
}