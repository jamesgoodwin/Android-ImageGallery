package com.imagegallery.list

import android.content.Context
import android.graphics.Point
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.imagegallery.R
import com.imagegallery.model.PhotoSearchResultItem

internal class ImageAdapter(private val images: List<PhotoSearchResultItem>, private val columns: Int) : RecyclerView.Adapter<ImageViewHolder>() {

    private var searchResultClickListener: OnPhotoSearchResultClickListener? = null

    internal interface OnPhotoSearchResultClickListener {
        fun onImageClicked(image: PhotoSearchResultItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.layout_list_image, parent, false)

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)

        return ImageViewHolder(view, searchResultClickListener!!, size.x / columns)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.setImage(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setOnImageClickListener(imageClickListener: OnPhotoSearchResultClickListener) {
        this.searchResultClickListener = imageClickListener
    }
}