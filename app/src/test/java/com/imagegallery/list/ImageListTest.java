package com.imagegallery.list;

import com.imagegallery.list.service.ImageService;
import com.imagegallery.model.PhotoSearchResult;
import com.imagegallery.model.PhotoSearchResultItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.UnknownHostException;

import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageListTest {

    @Mock
    private ImageListView imageListView;

    @Mock
    private ImageService imageService;

    @Mock
    private PhotoSearchResult photoSearchResult;

    @Test
    public void shouldDisplayImagesWhenRequested() {
        TestScheduler scheduler = new TestScheduler();

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
        TestScheduler scheduler = new TestScheduler();

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
        TestScheduler scheduler = new TestScheduler();

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
        TestScheduler scheduler = new TestScheduler();

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
}