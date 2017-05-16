package com.jamesgoodwin.flickrclient.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar.LENGTH_LONG
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import com.jamesgoodwin.flickrclient.R
import com.jamesgoodwin.flickrclient.fullscreen.FullscreenImageActivity
import com.jamesgoodwin.flickrclient.fullscreen.FullscreenImageActivity.Companion.IMAGE_DESCRIPTION_EXTRA
import com.jamesgoodwin.flickrclient.fullscreen.FullscreenImageActivity.Companion.IMAGE_TITLE_EXTRA
import com.jamesgoodwin.flickrclient.fullscreen.FullscreenImageActivity.Companion.IMAGE_URL_EXTRA
import com.jamesgoodwin.flickrclient.list.ImageListPresenter.Companion.DATE_PUBLISHED_DESC_SORT_TYPE
import com.jamesgoodwin.flickrclient.list.ImageListPresenter.Companion.DATE_TAKEN_DESC_SORT_TYPE
import com.jamesgoodwin.flickrclient.list.service.FlickrImageService
import com.jamesgoodwin.flickrclient.list.service.FlickrImageService.Companion.FLICKR_BASE_URL
import com.jamesgoodwin.flickrclient.list.service.FlickrRetrofitApiService
import com.jamesgoodwin.flickrclient.model.PhotoSearchResultItem
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_image_list.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ImageListActivity : AppCompatActivity(), ImageListView {

    private var presenter: ImageListPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_list)
        setSupportActionBar(appBar)

        // simple method to allow the FlickrImageService to be easily tested
        // creating the dependencies outside of the service.
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(FLICKR_BASE_URL)
                .client(OkHttpClient())
                .build()

        val apiService = retrofit.create(FlickrRetrofitApiService::class.java)
        val imageService = FlickrImageService(apiService)

        this.presenter = ImageListPresenter(this, imageService, mainThread(), Schedulers.io())

        if(savedInstanceState == null) {
            this.presenter!!.requestImages()
        }

        initialiseSearchView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_images_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_menu_item -> {
                showSortTypeDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showSortTypeDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.sort_results_by)
                .setItems(R.array.sort_type_array) { dialog, which ->
                    when (which) {
                        DATE_TAKEN_DIALOG_OPTION_POSITION -> presenter!!.showSortedSearchResults(DATE_TAKEN_DESC_SORT_TYPE)
                        else -> presenter!!.showSortedSearchResults(DATE_PUBLISHED_DESC_SORT_TYPE)
                    }
                }.create()
                .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.presenter!!.onDestroy()
    }

    override fun showLoading(show: Boolean) {
        loadingOverlay.visibility = if (show) VISIBLE else GONE
    }

    override fun showConnectionError() {
        Snackbar.make(imagesList, R.string.check_connection, LENGTH_LONG).show()
    }

    override fun showGenericError() {
        Snackbar.make(imagesList, R.string.error_retrieving_results, LENGTH_LONG).show()
    }

    private fun initialiseSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                hideKeyboard()
                presenter!!.requestImages(text)
                return true
            }

            override fun onQueryTextChange(text: String): Boolean {
                return true
            }
        })
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }

    override fun showImages(images: List<PhotoSearchResultItem>) {
        val columns = 3

        val adapter = ImageAdapter(images, columns)
        adapter.setOnImageClickListener(object : ImageAdapter.OnPhotoSearchResultClickListener {
            override fun onImageClicked(image: PhotoSearchResultItem) {
                val intent = Intent(this@ImageListActivity, FullscreenImageActivity::class.java)
                intent.putExtra(IMAGE_URL_EXTRA, image.url)
                intent.putExtra(IMAGE_TITLE_EXTRA, image.title)
                intent.putExtra(IMAGE_DESCRIPTION_EXTRA, image.toString())

                startActivity(intent)
            }
        })

        imagesList.adapter = adapter
        imagesList.layoutManager = GridLayoutManager(this@ImageListActivity, columns)
    }

    companion object {
        private val DATE_TAKEN_DIALOG_OPTION_POSITION = 1
    }
}