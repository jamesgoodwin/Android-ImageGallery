package com.imagegallery.list.service

import com.imagegallery.model.PhotoSearchResult

import io.reactivex.Single

interface ImageService {

    fun listPhotos(): Single<PhotoSearchResult>

    fun searchPhotos(query: String): Single<PhotoSearchResult>

}