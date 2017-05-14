package com.imagegallery.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.imagegallery.ImageSearchResult;

class ImageViewHolder extends RecyclerView.ViewHolder {

    public ImageViewHolder(View itemView) {
        super(itemView);

        itemView.findViewById(R.id.image);
    }

    public void setImage(ImageSearchResult imageSearchResult) {

    }
}
