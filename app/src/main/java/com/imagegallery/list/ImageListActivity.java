package com.imagegallery.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.imagegallery.FlickrImageService;
import com.imagegallery.ImageService;
import com.imagegallery.PhotoSearchResult;
import com.imagegallery.R;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class ImageListActivity extends AppCompatActivity {

    private RecyclerView imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.imagesList = (RecyclerView) findViewById(R.id.images_list);

        ImageService imageService = new FlickrImageService();

        imageService.listPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(mainThread())
                .subscribe(new Action1<PhotoSearchResult>() {
                    @Override
                    public void call(PhotoSearchResult photoSearchResult) {
                        imagesList.setAdapter(new ImageAdapter(photoSearchResult.getItems()));
                    }
                });
    }

}