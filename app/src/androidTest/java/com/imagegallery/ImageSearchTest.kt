package com.imagegallery

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.imagegallery.list.ImageListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageSearchTest {

    @Rule
    var activityTestRule = ActivityTestRule(ImageListActivity::class.java, false, false)

    @Test
    fun shouldDisplayResultsWhenSearchPerformed() {
        activityTestRule.launchActivity(Intent())

        // when I type a search term
        onView(withId(R.id.searchView))
                .perform(typeText("dogs"))

        // check there is a single result that can be clicked
        onView(withId(R.id.imagesList))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

}