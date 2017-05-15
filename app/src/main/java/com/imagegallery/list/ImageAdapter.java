package com.imagegallery.list;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.imagegallery.R;
import com.imagegallery.model.PhotoSearchResultItem;

import java.util.List;

class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private final int columns;
    private final List<PhotoSearchResultItem> images;

    private OnPhotoSearchResultClickListener searchResultClickListener;

    interface OnPhotoSearchResultClickListener {
        void onImageClicked(PhotoSearchResultItem image);
    }

    ImageAdapter(List<PhotoSearchResultItem> images, int columns) {
        this.images = images;
        this.columns = columns;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.layout_list_image, parent, false);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return new ImageViewHolder(view, searchResultClickListener, size.x / columns);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.setImage(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    void setOnImageClickListener(OnPhotoSearchResultClickListener imageClickListener) {
        this.searchResultClickListener = imageClickListener;
    }
}