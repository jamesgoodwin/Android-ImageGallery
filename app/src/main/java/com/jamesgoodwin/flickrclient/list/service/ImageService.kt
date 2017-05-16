package com.jamesgoodwin.flickrclient.list.service

import com.jamesgoodwin.flickrclient.model.PhotoSearchResult

import io.reactivex.Single

interface ImageService {

    fun listPhotos(): Single<PhotoSearchResult>

    fun searchPhotos(query: String): Single<PhotoSearchResult>

}