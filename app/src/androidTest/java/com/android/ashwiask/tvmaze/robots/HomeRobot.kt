package com.android.ashwiask.tvmaze.robots

import android.app.Activity
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.android.ashwiask.tvmaze.R
import com.android.ashwiask.tvmaze.home.ShowsAdapter
import org.hamcrest.Matcher
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
                    (0, clickChildViewWithId(R.id.favorite))
            )
    }

    fun verifyToast(activity: Activity){
        onView(withText(R.string.added_to_favorites)).inRoot(withDecorView(not(`is`(activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    private fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }
}