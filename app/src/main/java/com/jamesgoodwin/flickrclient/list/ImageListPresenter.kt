package com.jamesgoodwin.flickrclient.list

import com.jamesgoodwin.flickrclient.list.service.ImageService
import com.jamesgoodwin.flickrclient.model.PhotoSearchResult
import com.jamesgoodwin.flickrclient.model.PhotoSearchResultItem
import io.reactivex.Scheduler
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.net.UnknownHostException
import java.util.*

internal class ImageListPresenter(
        private var view: ImageListView?,
        private val imageService: ImageService,
        private val viewScheduler: Scheduler,
        private val backgroundScheduler: Scheduler) {

    private var currentSearchResults: PhotoSearchResult? = null

    fun requestImages() {
        view!!.showLoading(true)

        imageService.listPhotos()
                .subscribeOn(backgroundScheduler)
                .observeOn(viewScheduler)
                .doAfterTerminate(hideLoading())
                .subscribe(onSuccess(), onError())
    }

    fun requestImages(query: String) {
        view!!.showLoading(true)

        imageService.searchPhotos(query)
                .subscribeOn(backgroundScheduler)
                .observeOn(viewScheduler)
                .doAfterTerminate(hideLoading())
                .subscribe(onSuccess(), onError())
    }

    private fun onSuccess(): Consumer<PhotoSearchResult> {
        return Consumer { photoSearchResult ->
            currentSearchResults = photoSearchResult
            view!!.showImages(photoSearchResult.items)
        }
    }

    private fun onError(): Consumer<Throwable> {
        return Consumer { throwable ->
            if (throwable is UnknownHostException) {
                view!!.showConnectionError()
            } else {
                view!!.showGenericError()
            }
        }
    }

    private fun hideLoading(): Action {
        return Action { view!!.showLoading(false) }
    }

    fun onDestroy() {
        this.view = null
    }

    fun showSortedSearchResults(sortType: Int) {
        if (currentSearchResults != null) {
            val searchResultItems = currentSearchResults!!.items

            when (sortType) {
                DATE_TAKEN_DESC_SORT_TYPE -> sortByDateTaken(searchResultItems)
                else -> sortByDatePublished(searchResultItems)
            }

            view!!.showImages(searchResultItems)
        }
    }

    private fun sortByDatePublished(searchResults: List<PhotoSearchResultItem>) {
        Collections.sort(searchResults) { result1, result2 -> result2.datePublished.compareTo(result1.datePublished) }
    }

    private fun sortByDateTaken(searchResults: List<PhotoSearchResultItem>) {
        Collections.sort(searchResults) { result1, result2 -> result2.dateTaken.compareTo(result1.dateTaken) }
    }

    companion object {
        val DATE_TAKEN_DESC_SORT_TYPE = 0
        val DATE_PUBLISHED_DESC_SORT_TYPE = 1
    }
}