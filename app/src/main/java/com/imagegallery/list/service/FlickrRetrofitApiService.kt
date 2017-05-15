package com.imagegallery.list.service

import com.imagegallery.model.PhotoSearchResult

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrRetrofitApiService {

    @GET(SEARCH_PATH)
    fun listPhotos(): Single<PhotoSearchResult>

    @GET(SEARCH_PATH)
    fun searchPhotos(@Query("tags") tags: String): Single<PhotoSearchResult>

    companion object {
        const val SEARCH_PATH = "/services/feeds/photos_public.gne?format=json&nojsoncallback=true"
    }

}