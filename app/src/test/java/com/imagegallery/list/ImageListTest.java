package com.imagegallery.list;

import com.imagegallery.list.service.ImageService;
import com.imagegallery.model.PhotoSearchResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Single;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageListTest {

    @Mock
    private ImageListView imageListView;

    @Mock
    private ImageService imageService;

    @Test
    public void shouldDisplayImagesWhenRequested() {
        ImageListPresenter presenter = new ImageListPresenter(imageListView, imageService);

        when(imageService.listPhotos())
                .thenReturn(Single.just(new PhotoSearchResult()));

        presenter.requestImages();

        verify(imageListView, times(1)).showImages(anyListOf(ImageSearchResult.class));

    }

}