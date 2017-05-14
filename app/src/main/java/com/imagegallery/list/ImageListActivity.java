package com.imagegallery.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.imagegallery.R;
import com.imagegallery.list.service.FlickrImageService;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ImageListActivity extends AppCompatActivity implements ImageListView {

    private RecyclerView imagesList;
    private SearchView searchView;
    private View loadingOverlay;

    private ImageListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.imagesList = (RecyclerView) findViewById(R.id.images_list);
        this.searchView = (SearchView) findViewById(R.id.search_view);
        this.loadingOverlay = findViewById(R.id.loading_overlay);

        this.presenter = new ImageListPresenter(this, new FlickrImageService());
        this.presenter.requestImages();

        initialiseSearchView();
        hideKeyboard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.onDestroy();
    }

    @Override
    public void showImages(List<? extends ImageSearchResult> images) {
        int columns = 3;

        ImageAdapter adapter = new ImageAdapter(images, columns);
        imagesList.setAdapter(adapter);
        imagesList.setLayoutManager(new GridLayoutManager(ImageListActivity.this, columns));
    }

    @Override
    public void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? VISIBLE : GONE);
    }

    private void initialiseSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                hideKeyboard();
                presenter.requestImages(text);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                return true;
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

}