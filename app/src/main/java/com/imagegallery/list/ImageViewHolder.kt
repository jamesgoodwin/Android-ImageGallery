package com.imagegallery.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView

import com.imagegallery.R
import com.imagegallery.model.PhotoSearchResultItem
import com.squareup.picasso.Picasso

internal class ImageViewHolder(
        itemView: View,
        private val imageClickListener: ImageAdapter.OnPhotoSearchResultClickListener,
        private val imageSize: Int) : RecyclerView.ViewHolder(itemView) {

    private val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView) as ImageView

    fun setImage(image: PhotoSearchResultItem) {
        Picasso.with(photoImageView.context)
                .load(image.url)
                .centerCrop()
                .resize(imageSize, imageSize)
                .into(photoImageView)

        photoImageView.setOnClickListener { imageClickListener.onImageClicked(image) }
        photoImageView.contentDescription = image.title
    }
}
