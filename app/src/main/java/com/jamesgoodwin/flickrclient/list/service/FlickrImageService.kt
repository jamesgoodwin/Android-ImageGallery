package com.jamesgoodwin.flickrclient.list.service

import com.jamesgoodwin.flickrclient.model.PhotoSearchResult

import io.reactivex.Single

class FlickrImageService(private val apiService: FlickrRetrofitApiService) : ImageService {

    override fun listPhotos(): Single<PhotoSearchResult> {
        return apiService.listPhotos()
    }

    override fun searchPhotos(query: String): Single<PhotoSearchResult> {
        val formattedQuery = query.replace("/[,\\s]/".toRegex(), ",")

        return apiService.searchPhotos(formattedQuery)
    }

    companion object {
        val FLICKR_BASE_URL = "https://api.flickr.com"
    }
}