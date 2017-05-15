package com.imagegallery.fullscreen;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.imagegallery.R;
import com.squareup.picasso.Picasso;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;

public class FullscreenImageActivity extends AppCompatActivity {

    public static final String IMAGE_URL_EXTRA = "ImageUrl";
    public static final String IMAGE_TITLE_EXTRA = "ImageTitle";

    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler hideHandler = new Handler();

    private ImageView imageView;

    private final Runnable hidePart2Runnable = new Runnable() {
        @Override
        public void run() {
            imageView.setSystemUiVisibility(SYSTEM_UI_FLAG_LOW_PROFILE
                    | SYSTEM_UI_FLAG_FULLSCREEN
                    | SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable showPart2Runnable = () -> {
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    };
    private boolean visible = true;
    private final Runnable hideRunnable = this::hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_image);

        imageView = (ImageView) findViewById(R.id.image_view);

        setTitle(getIntent().getStringExtra(IMAGE_TITLE_EXTRA));

        Picasso.with(this)
                .load(getIntent().getStringExtra(IMAGE_URL_EXTRA))
                .fit().centerInside()
                .into(imageView);

        imageView.setOnClickListener(view -> toggle());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (visible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        visible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable);
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        visible = true;

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable);
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        hideHandler.removeCallbacks(hideRunnable);
        hideHandler.postDelayed(hideRunnable, delayMillis);
    }
}
