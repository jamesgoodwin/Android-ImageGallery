package com.imagegallery.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.imagegallery.R;
import com.imagegallery.model.PhotoSearchResultItem;
import com.squareup.picasso.Picasso;

class ImageViewHolder extends RecyclerView.ViewHolder {

    private final ImageView imageView;
    private final int imageSize;

    private final ImageAdapter.OnPhotoSearchResultClickListener imageClickListener;

    ImageViewHolder(View itemView, ImageAdapter.OnPhotoSearchResultClickListener imageClickListener, int imageSize) {
        super(itemView);

        this.imageSize = imageSize;
        this.imageClickListener = imageClickListener;
        this.imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    void setImage(PhotoSearchResultItem image) {
        Picasso.with(imageView.getContext())
                .load(image.getUrl())
                .centerCrop()
                .resize(imageSize, imageSize)
                .into(imageView);

        imageView.setOnClickListener(view -> imageClickListener.onImageClicked(image));
        imageView.setContentDescription(image.getTitle());
    }
}
