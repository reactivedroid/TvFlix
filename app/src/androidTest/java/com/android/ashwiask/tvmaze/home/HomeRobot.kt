package com.android.ashwiask.tvmaze.home

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.android.ashwiask.tvmaze.R
import com.android.ashwiask.tvmaze.utils.MatcherUtils
import com.android.ashwiask.tvmaze.utils.ViewActionUtils
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not

fun launchHome(func: HomeRobot.() -> Unit) = HomeRobot().apply { func() }
class HomeRobot {
    fun verifyHome() {
        onView(withId(R.id.popular_show_header)).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.popular_shows), isDisplayed()))
    }

    fun verifyFavorite() {
        // Click on 1st item and mark as favorite
        onView(withId(R.id.popular_shows))
            .perform(
                actionOnItemAtPosition<ShowsAdapter.ShowHolder>
                    (0, ViewActionUtils.clickChildViewWithId(R.id.favorite))
            )
    }

    fun verifyToast(activity: Activity) {
        onView(withText(R.string.added_to_favorites)).inRoot(withDecorView(not(`is`(activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    fun verifyFavoriteScreen() {
        onView(withId(R.id.action_favorites))
            .perform(click())
        onView(withText(R.string.favorite_shows)).check(matches(isDisplayed()))
        onView(withId(R.id.shows))
            .check(matches(MatcherUtils.withListSize(1)))
    }
}