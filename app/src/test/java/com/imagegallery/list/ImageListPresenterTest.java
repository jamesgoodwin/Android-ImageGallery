package com.imagegallery.list;

import com.imagegallery.list.service.ImageService;
import com.imagegallery.model.PhotoSearchResult;
import com.imagegallery.model.PhotoSearchResultItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;

import static com.imagegallery.list.service.ImageService.DATE_PUBLISHED_DESC_SORT_TYPE;
import static com.imagegallery.list.service.ImageService.DATE_TAKEN_DESC_SORT_TYPE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageListPresenterTest {

    @Mock
    private ImageListView imageListView;

    @Mock
    private ImageService imageService;

    @Mock
    private PhotoSearchResult photoSearchResult;

    @Captor
    private ArgumentCaptor<List<PhotoSearchResultItem>> captor;

    private Date dateNow = new Date();
    private TestScheduler scheduler = new TestScheduler();

    @Test
    public void shouldDisplayImagesWhenRequested() {
        ImageListPresenter presenter = new ImageListPresenter(imageListView, imageService, scheduler, scheduler);

        when(imageService.listPhotos())
                .thenReturn(Single.just(photoSearchResult));

        presenter.requestImages();

        scheduler.triggerActions();

        verify(imageListView, times(1))
                .showLoading(eq(true));

        verify(imageListView, times(1))
                .showImages(anyListOf(PhotoSearchResultItem.class));

        verify(imageListView, times(1))
                .showLoading(eq(false));
    }

    @Test
    public void shouldDisplayImageSearchResultsWhenRequested() {
        ImageListPresenter presenter = new ImageListPresenter(imageListView, imageService, scheduler, scheduler);

        String searchTerm = "dogs";

        when(imageService.searchPhotos(eq(searchTerm)))
                .thenReturn(Single.just(photoSearchResult));

        presenter.requestImages(searchTerm);

        scheduler.triggerActions();

        verify(imageListView, times(1))
                .showLoading(eq(true));

        verify(imageListView, times(1))
                .showImages(anyListOf(PhotoSearchResultItem.class));

        verify(imageListView, times(1))
                .showLoading(eq(false));
    }

    @Test
    public void shouldDisplayConnectionErrorMessageWhenNoInternetAndListingPhotos() {
        ImageListPresenter presenter = new ImageListPresenter(imageListView, imageService, scheduler, scheduler);

        when(imageService.listPhotos())
                .thenReturn(Single.error(new UnknownHostException()));

        presenter.requestImages();

        scheduler.triggerActions();

        verify(imageListView, times(1))
                .showLoading(eq(true));

        verify(imageListView, times(1))
                .showConnectionError();

        verify(imageListView, times(1))
                .showLoading(eq(false));
    }

    @Test
    public void shouldDisplayConnectionErrorMessageWhenNoInternetAndSearching() {
        ImageListPresenter presenter = new ImageListPresenter(imageListView, imageService, scheduler, scheduler);

        String searchTerm = "dogs";

        when(imageService.searchPhotos(eq(searchTerm)))
                .thenReturn(Single.error(new UnknownHostException()));

        presenter.requestImages(searchTerm);

        scheduler.triggerActions();

        verify(imageListView, times(1))
                .showLoading(eq(true));

        verify(imageListView, times(1))
                .showConnectionError();

        verify(imageListView, times(1))
                .showLoading(eq(false));
    }

    @Test
    public void shouldReorderPhotosByDateTaken() {
        ImageListPresenter presenter = new ImageListPresenter(imageListView, imageService, scheduler, scheduler);

        ArrayList<PhotoSearchResultItem> searchResultItems = new ArrayList<>();

        PhotoSearchResultItem firstTakenPhoto = new PhotoSearchResultItem("taken 1st", null, null, yesterday(), dateNow);
        PhotoSearchResultItem secondTakenPhoto = new PhotoSearchResultItem("taken 2nd", null, null, dateNow, dateNow);

        searchResultItems.add(firstTakenPhoto);
        searchResultItems.add(secondTakenPhoto);

        PhotoSearchResult photoSearchResult = new PhotoSearchResult(null, searchResultItems);

        when(imageService.listPhotos())
                .thenReturn(Single.just(photoSearchResult));

        presenter.requestImages();

        scheduler.triggerActions();

        presenter.showSortedSearchResults(DATE_TAKEN_DESC_SORT_TYPE);

        scheduler.triggerActions();

        verify(imageListView, times(2))
                .showImages(captor.capture());

        assertThat(captor.getValue().get(0), equalTo(secondTakenPhoto));
        assertThat(captor.getValue().get(1), equalTo(firstTakenPhoto));
    }

    @Test
    public void shouldReorderPhotosByDatePublished() {
        ImageListPresenter presenter = new ImageListPresenter(imageListView, imageService, scheduler, scheduler);

        ArrayList<PhotoSearchResultItem> searchResultItems = new ArrayList<>();

        PhotoSearchResultItem firstPublishedPhoto = new PhotoSearchResultItem("published 1st", null, null, yesterday(), yesterday());
        PhotoSearchResultItem secondPublishedPhoto = new PhotoSearchResultItem("published 2nd", null, null, yesterday(), dateNow);

        searchResultItems.add(firstPublishedPhoto);
        searchResultItems.add(secondPublishedPhoto);

        PhotoSearchResult photoSearchResult = new PhotoSearchResult(null, searchResultItems);

        when(imageService.listPhotos())
                .thenReturn(Single.just(photoSearchResult));

        presenter.requestImages();

        scheduler.triggerActions();

        presenter.showSortedSearchResults(DATE_PUBLISHED_DESC_SORT_TYPE);

        scheduler.triggerActions();

        verify(imageListView, times(2))
                .showImages(captor.capture());

        assertThat(captor.getValue().get(0), equalTo(secondPublishedPhoto));
        assertThat(captor.getValue().get(1), equalTo(firstPublishedPhoto));
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

}