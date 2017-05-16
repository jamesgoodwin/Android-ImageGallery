package com.jamesgoodwin.flickrclient.list

import com.jamesgoodwin.flickrclient.model.PhotoSearchResultItem

internal interface ImageListView {

    fun showImages(images: List<PhotoSearchResultItem>)

    fun showLoading(show: Boolean)

    fun showConnectionError()

    fun showGenericError()
}