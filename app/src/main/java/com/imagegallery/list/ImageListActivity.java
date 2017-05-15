package com.imagegallery.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.imagegallery.R;
import com.imagegallery.fullscreen.FullscreenImageActivity;
import com.imagegallery.list.service.FlickrImageService;
import com.imagegallery.list.service.FlickrRetrofitApiService;
import com.imagegallery.model.PhotoSearchResultItem;

import java.util.List;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.design.widget.BaseTransientBottomBar.LENGTH_LONG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.imagegallery.fullscreen.FullscreenImageActivity.IMAGE_TITLE_EXTRA;
import static com.imagegallery.fullscreen.FullscreenImageActivity.IMAGE_URL_EXTRA;
import static com.imagegallery.list.service.FlickrImageService.FLICKR_BASE_URL;
import static com.imagegallery.list.service.ImageService.DATE_PUBLISHED_DESC_SORT_TYPE;
import static com.imagegallery.list.service.ImageService.DATE_TAKEN_DESC_SORT_TYPE;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public class ImageListActivity extends AppCompatActivity implements ImageListView {

    private static final int DATE_TAKEN_DIALOG_OPTION_POSITION = 1;

    private RecyclerView imagesList;
    private SearchView searchView;
    private View loadingOverlay;

    private ImageListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_list);

        this.searchView = (SearchView) findViewById(R.id.search_view);
        this.imagesList = (RecyclerView) findViewById(R.id.images_list);
        this.loadingOverlay = findViewById(R.id.loading_overlay);

        setSupportActionBar((Toolbar) findViewById(R.id.app_bar));

        // simple method to allow the FlickrImageService to be easily tested
        // creating the dependencies outside of the service.
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(FLICKR_BASE_URL)
                .client(new OkHttpClient())
                .build();

        FlickrRetrofitApiService apiService = retrofit.create(FlickrRetrofitApiService.class);
        FlickrImageService imageService = new FlickrImageService(apiService);

        this.presenter = new ImageListPresenter(this, imageService, mainThread(), Schedulers.io());
        this.presenter.requestImages();

        initialiseSearchView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_images_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_menu_item:
                showSortTypeDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortTypeDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sort_results_by)
                .setItems(R.array.sort_type_array, (dialog, which) -> {
                    switch (which) {
                        case DATE_TAKEN_DIALOG_OPTION_POSITION:
                            presenter.showSortedSearchResults(DATE_TAKEN_DESC_SORT_TYPE);
                            break;
                        default:
                            presenter.showSortedSearchResults(DATE_PUBLISHED_DESC_SORT_TYPE);
                            break;
                    }
                }).create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.onDestroy();
    }

    @Override
    public void showImages(List<PhotoSearchResultItem> images) {
        int columns = 3;

        ImageAdapter adapter = new ImageAdapter(images, columns);

        adapter.setOnImageClickListener(image -> {
            Intent intent = new Intent(ImageListActivity.this, FullscreenImageActivity.class);
            intent.putExtra(IMAGE_URL_EXTRA, image.getUrl());
            intent.putExtra(IMAGE_TITLE_EXTRA, image.getDescription());

            startActivity(intent);
        });

        imagesList.setAdapter(adapter);
        imagesList.setLayoutManager(new GridLayoutManager(ImageListActivity.this, columns));
    }

    @Override
    public void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? VISIBLE : GONE);
    }

    @Override
    public void showConnectionError() {
        Snackbar.make(imagesList, R.string.check_connection, LENGTH_LONG).show();
    }

    @Override
    public void showGenericError() {
        Snackbar.make(imagesList, R.string.error_retrieving_results, LENGTH_LONG).show();
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
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

}