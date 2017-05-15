package com.imagegallery.list

import com.imagegallery.model.PhotoSearchResultItem

internal interface ImageListView {

    fun showImages(images: List<PhotoSearchResultItem>)

    fun showLoading(show: Boolean)

    fun showConnectionError()

    fun showGenericError()
}