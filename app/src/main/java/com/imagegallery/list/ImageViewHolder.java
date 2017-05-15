package com.imagegallery.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.imagegallery.R;
import com.squareup.picasso.Picasso;

class ImageViewHolder extends RecyclerView.ViewHolder {

    private final ImageView imageView;
    private final int imageSize;

    private final ImageAdapter.OnImageClickListener imageClickListener;

    ImageViewHolder(View itemView, ImageAdapter.OnImageClickListener imageClickListener, int imageSize) {
        super(itemView);

        this.imageSize = imageSize;
        this.imageClickListener = imageClickListener;
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    void setImage(final ImageSearchResult image) {
        Picasso.with(imageView.getContext())
                .load(image.getUrl())
                .centerCrop()
                .resize(imageSize, imageSize)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClickListener.onImageClicked(image);
            }
        });

        imageView.setContentDescription(image.getDescription());
    }
}
