package com.imagegallery;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.imagegallery.list.ImageListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ImageSearchTest {

    @Rule
    public ActivityTestRule<ImageListActivity> activityTestRule = new ActivityTestRule<>(ImageListActivity.class, false, false);

    @Test
    public void shouldDisplayResultsWhenSearchPerformed() {
        activityTestRule.launchActivity(new Intent());

        // when I type a search term
        onView(withId(R.id.search_view))
                .perform(typeText("dogs"));

        // check there is a single result that can be clicked
        onView(withId(R.id.images_list))
                .perform(actionOnItemAtPosition(0, click()));
    }

}