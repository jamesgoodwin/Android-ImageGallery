package com.imagegallery.fullscreen

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.*
import com.imagegallery.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fullscreen_image.*

class FullscreenImageActivity : AppCompatActivity() {

    private val hideHandler = Handler()
    private var hidePart2Runnable: Runnable? = null
    private var showPart2Runnable: Runnable? = null

    private var actionBarVisible = true
    private val hideRunnable = Runnable { hide() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen_image)

        descriptionText.text = intent.getStringExtra(IMAGE_DESCRIPTION_EXTRA)
        title = intent.getStringExtra(IMAGE_TITLE_EXTRA)

        Picasso.with(this)
                .load(intent.getStringExtra(IMAGE_URL_EXTRA))
                .fit().centerInside()
                .into(imageView)

        imageView.setOnClickListener { this@FullscreenImageActivity.toggle() }

        hidePart2Runnable = Runnable { imageView.systemUiVisibility = SYSTEM_UI_FLAG_LOW_PROFILE or SYSTEM_UI_FLAG_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or SYSTEM_UI_FLAG_HIDE_NAVIGATION }

        showPart2Runnable = Runnable {
            supportActionBar?.show()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)
    }

    private fun toggle() {
        if (actionBarVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        val actionBar = supportActionBar
        actionBar?.hide()
        actionBarVisible = false

        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        imageView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        actionBarVisible = true

        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        val IMAGE_URL_EXTRA = "ImageUrl"
        val IMAGE_TITLE_EXTRA = "ImageTitle"
        val IMAGE_DESCRIPTION_EXTRA = "ImageDescription"

        private val UI_ANIMATION_DELAY = 300
    }
}
