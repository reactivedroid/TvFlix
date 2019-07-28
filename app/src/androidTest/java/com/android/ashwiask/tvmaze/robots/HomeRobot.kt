package com.android.ashwiask.tvmaze.robots

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.android.ashwiask.tvmaze.R
import com.android.ashwiask.tvmaze.home.ShowsAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher

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
                    (0, clickChildViewWithId(R.id.favorite))
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
            .check(matches(withListSize(1)))
    }

    fun verifyAllShows(){
        onView(withId(R.id.action_shows))
            .perform(click())
        onView(withText(R.string.shows)).check(matches(isDisplayed()))
        onView(withId(R.id.shows))
            .check(matches(withListSize(1)))
    }

    private fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with id $id"
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }

    private fun withListSize(size: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(view: View): Boolean {
                return (view as RecyclerView).adapter!!.itemCount >= size
            }

            override fun describeTo(description: Description) {
                description.appendText("RecyclerView should have $size items")
            }
        }
    }
}