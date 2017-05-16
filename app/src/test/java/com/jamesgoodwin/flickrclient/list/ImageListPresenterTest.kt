package com.jamesgoodwin.flickrclient.list

import com.jamesgoodwin.flickrclient.list.service.ImageService
import com.jamesgoodwin.flickrclient.model.PhotoMedia
import com.jamesgoodwin.flickrclient.model.PhotoSearchResult
import com.jamesgoodwin.flickrclient.model.PhotoSearchResultItem
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Matchers.anyListOf
import org.mockito.Matchers.eq
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import java.net.UnknownHostException
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ImageListPresenterTest {

    @Mock
    private val imageListView: ImageListView? = null

    @Mock
    private val imageService: ImageService? = null

    @Mock
    private val photoSearchResult: PhotoSearchResult? = null

    @Captor
    private val captor: ArgumentCaptor<List<PhotoSearchResultItem>>? = null

    private val dateNow = Date()
    private val scheduler = TestScheduler()

    @Test
    fun shouldDisplayImagesWhenRequested() {
        val presenter = ImageListPresenter(imageListView, imageService!!, scheduler, scheduler)

        `when`(imageService.listPhotos())
                .thenReturn(Single.just(photoSearchResult!!))

        presenter.requestImages()

        scheduler.triggerActions()

        verify<ImageListView>(imageListView, times(1))
                .showLoading(eq(true))

        verify<ImageListView>(imageListView, times(1))
                .showImages(anyListOf(PhotoSearchResultItem::class.java))

        verify<ImageListView>(imageListView, times(1))
                .showLoading(eq(false))
    }

    @Test
    fun shouldDisplayImageSearchResultsWhenRequested() {
        val presenter = ImageListPresenter(imageListView, imageService!!, scheduler, scheduler)

        val searchTerm = "dogs"

        `when`(imageService.searchPhotos(eq(searchTerm)))
                .thenReturn(Single.just(photoSearchResult!!))

        presenter.requestImages(searchTerm)

        scheduler.triggerActions()

        verify<ImageListView>(imageListView, times(1))
                .showLoading(eq(true))

        verify<ImageListView>(imageListView, times(1))
                .showImages(anyListOf(PhotoSearchResultItem::class.java))

        verify<ImageListView>(imageListView, times(1))
                .showLoading(eq(false))
    }

    @Test
    fun shouldDisplayConnectionErrorMessageWhenNoInternetAndListingPhotos() {
        val presenter = ImageListPresenter(imageListView, imageService!!, scheduler, scheduler)

        `when`(imageService.listPhotos())
                .thenReturn(Single.error<PhotoSearchResult>(UnknownHostException()))

        presenter.requestImages()

        scheduler.triggerActions()

        verify<ImageListView>(imageListView, times(1))
                .showLoading(eq(true))

        verify<ImageListView>(imageListView, times(1))
                .showConnectionError()

        verify<ImageListView>(imageListView, times(1))
                .showLoading(eq(false))
    }

    @Test
    fun shouldDisplayConnectionErrorMessageWhenNoInternetAndSearching() {
        val presenter = ImageListPresenter(imageListView, imageService!!, scheduler, scheduler)

        val searchTerm = "dogs"

        `when`(imageService.searchPhotos(eq(searchTerm)))
                .thenReturn(Single.error<PhotoSearchResult>(UnknownHostException()))

        presenter.requestImages(searchTerm)

        scheduler.triggerActions()

        verify<ImageListView>(imageListView, times(1))
                .showLoading(eq(true))

        verify<ImageListView>(imageListView, times(1))
                .showConnectionError()

        verify<ImageListView>(imageListView, times(1))
                .showLoading(eq(false))
    }

    @Test
    fun shouldReorderPhotosByDateTaken() {
        val presenter = ImageListPresenter(imageListView, imageService!!, scheduler, scheduler)

        val searchResultItems = ArrayList<PhotoSearchResultItem>()

        val firstTakenPhoto = PhotoSearchResultItem("taken 1st", "", PhotoMedia(""), yesterday(), dateNow)
        val secondTakenPhoto = PhotoSearchResultItem("taken 2nd", "", PhotoMedia(""), dateNow, dateNow)

        searchResultItems.add(firstTakenPhoto)
        searchResultItems.add(secondTakenPhoto)

        val photoSearchResult = PhotoSearchResult("", searchResultItems)

        `when`(imageService.listPhotos())
                .thenReturn(Single.just(photoSearchResult))

        presenter.requestImages()

        scheduler.triggerActions()

        presenter.showSortedSearchResults(ImageListPresenter.Companion.DATE_TAKEN_DESC_SORT_TYPE)

        scheduler.triggerActions()

        verify<ImageListView>(imageListView, times(2))
                .showImages(captor!!.capture())

        assertThat(captor.value[0], equalTo(secondTakenPhoto))
        assertThat(captor.value[1], equalTo(firstTakenPhoto))
    }

    @Test
    fun shouldReorderPhotosByDatePublished() {
        val presenter = ImageListPresenter(imageListView, imageService!!, scheduler, scheduler)

        val searchResultItems = ArrayList<PhotoSearchResultItem>()

        val firstPublishedPhoto = PhotoSearchResultItem("published 1st", "", PhotoMedia(""), yesterday(), yesterday())
        val secondPublishedPhoto = PhotoSearchResultItem("published 2nd", "", PhotoMedia(""), yesterday(), dateNow)

        searchResultItems.add(firstPublishedPhoto)
        searchResultItems.add(secondPublishedPhoto)

        val photoSearchResult = PhotoSearchResult("", searchResultItems)

        `when`(imageService.listPhotos())
                .thenReturn(Single.just(photoSearchResult))

        presenter.requestImages()

        scheduler.triggerActions()

        presenter.showSortedSearchResults(ImageListPresenter.Companion.DATE_PUBLISHED_DESC_SORT_TYPE)

        scheduler.triggerActions()

        verify<ImageListView>(imageListView, times(2))
                .showImages(captor!!.capture())

        assertThat(captor.value[0], equalTo(secondPublishedPhoto))
        assertThat(captor.value[1], equalTo(firstPublishedPhoto))
    }

    private fun yesterday(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        return cal.time
    }

}