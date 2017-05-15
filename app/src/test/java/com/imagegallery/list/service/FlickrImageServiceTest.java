package com.imagegallery.list.service;

import com.imagegallery.model.PhotoSearchResult;
import com.imagegallery.model.PhotoSearchResultItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Single;

import static com.imagegallery.list.service.ImageService.DATE_TAKEN_DESC_SORT_TYPE;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlickrImageServiceTest {

    @Mock
    private FlickrRetrofitApiService apiService;

    @InjectMocks
    private FlickrImageService flickrImageService;

    private Date dateNow = new Date();

    @Test
    public void shouldOrderPhotosByDatePublishedByDefaultWhenListingPhotos() {
        ArrayList<PhotoSearchResultItem> searchResultItems = new ArrayList<>();

        PhotoSearchResultItem firstPublishedPhoto = new PhotoSearchResultItem("published 1st", null, null, yesterday(), yesterday());
        PhotoSearchResultItem secondPublishedPhoto = new PhotoSearchResultItem("published 2nd", null, null, yesterday(), dateNow);

        searchResultItems.add(firstPublishedPhoto);
        searchResultItems.add(secondPublishedPhoto);

        PhotoSearchResult photoSearchResult = new PhotoSearchResult(null, searchResultItems);

        when(apiService.listPhotos())
                .thenReturn(Single.just(photoSearchResult));

        flickrImageService.listPhotos()
                .test()
                .assertValueAt(0, searchResult -> searchResult.getItems().get(0).equals(secondPublishedPhoto))
                .assertValueAt(0, searchResult -> searchResult.getItems().get(1).equals(firstPublishedPhoto));
    }

    @Test
    public void shouldOrderPhotosByDateTakenWhenListingPhotosWithSortType() {
        ArrayList<PhotoSearchResultItem> searchResultItems = new ArrayList<>();

        PhotoSearchResultItem firstTakenPhoto = new PhotoSearchResultItem("taken 1st", null, null, yesterday(), dateNow);
        PhotoSearchResultItem secondTakenPhoto = new PhotoSearchResultItem("taken 2nd", null, null, dateNow, dateNow);

        searchResultItems.add(firstTakenPhoto);
        searchResultItems.add(secondTakenPhoto);

        PhotoSearchResult photoSearchResult = new PhotoSearchResult(null, searchResultItems);

        when(apiService.listPhotos())
                .thenReturn(Single.just(photoSearchResult));

        flickrImageService.listPhotos(DATE_TAKEN_DESC_SORT_TYPE)
                .test()
                .assertValueAt(0, searchResult -> searchResult.getItems().get(0).equals(secondTakenPhoto))
                .assertValueAt(0, searchResult -> searchResult.getItems().get(1).equals(firstTakenPhoto));
    }

    @Test
    public void shouldOrderPhotosByDatePublishedByDefaultWhenSearchingByTag() {
        ArrayList<PhotoSearchResultItem> searchResultItems = new ArrayList<>();

        PhotoSearchResultItem firstPublishedPhoto = new PhotoSearchResultItem("published 1st", null, null, yesterday(), yesterday());
        PhotoSearchResultItem secondPublishedPhoto = new PhotoSearchResultItem("published 2nd", null, null, yesterday(), dateNow);

        searchResultItems.add(firstPublishedPhoto);
        searchResultItems.add(secondPublishedPhoto);

        PhotoSearchResult photoSearchResult = new PhotoSearchResult(null, searchResultItems);
        String searchTerm = "dogs";

        when(apiService.searchPhotos(eq(searchTerm)))
                .thenReturn(Single.just(photoSearchResult));

        flickrImageService.searchPhotos(searchTerm)
                .test()
                .assertValueAt(0, searchResult -> searchResult.getItems().get(0).equals(secondPublishedPhoto))
                .assertValueAt(0, searchResult -> searchResult.getItems().get(1).equals(firstPublishedPhoto));
    }

    @Test
    public void shouldOrderPhotosByDateTakenWhenSearchingByTagWithSortType() {
        ArrayList<PhotoSearchResultItem> searchResultItems = new ArrayList<>();

        PhotoSearchResultItem firstTakenPhoto = new PhotoSearchResultItem("taken 1st", null, null, yesterday(), dateNow);
        PhotoSearchResultItem secondTakenPhoto = new PhotoSearchResultItem("taken 2nd", null, null, dateNow, dateNow);

        searchResultItems.add(firstTakenPhoto);
        searchResultItems.add(secondTakenPhoto);

        PhotoSearchResult photoSearchResult = new PhotoSearchResult(null, searchResultItems);

        String searchTerm = "dogs";
        when(apiService.searchPhotos(eq(searchTerm)))
                .thenReturn(Single.just(photoSearchResult));

        flickrImageService.searchPhotos(searchTerm, DATE_TAKEN_DESC_SORT_TYPE)
                .test()
                .assertValueAt(0, searchResult -> searchResult.getItems().get(0).equals(secondTakenPhoto))
                .assertValueAt(0, searchResult -> searchResult.getItems().get(1).equals(firstTakenPhoto));
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

}