package com.imagegallery.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.imagegallery.ImageSearchResult;
import com.imagegallery.R;

import java.util.List;

class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private final List<ImageSearchResult> images;

    public ImageAdapter(List<ImageSearchResult> images) {
        this.images = images;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ImageViewHolder(inflater.inflate(R.layout.layout_list_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.setImage(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

}
