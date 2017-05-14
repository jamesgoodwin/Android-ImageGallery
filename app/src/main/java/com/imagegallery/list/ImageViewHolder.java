package com.imagegallery.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.imagegallery.R;
import com.squareup.picasso.Picasso;

class ImageViewHolder extends RecyclerView.ViewHolder {

    private final ImageView imageView;
    private final int imageSize;

    ImageViewHolder(View itemView, int imageSize) {
        super(itemView);

        this.imageSize = imageSize;
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    void setImage(ImageSearchResult image) {
        Picasso.with(imageView.getContext())
                .load(image.getUrl())
                .centerCrop()
                .resize(imageSize, imageSize)
                .into(imageView);

        imageView.setContentDescription(image.getDescription());
    }
}
