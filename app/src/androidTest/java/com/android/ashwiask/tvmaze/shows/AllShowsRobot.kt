package com.android.ashwiask.tvmaze.shows

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.android.ashwiask.tvmaze.R
import com.android.ashwiask.tvmaze.utils.MatcherUtils

fun launchAllShows(func: AllShowsRobot.() -> Unit) = AllShowsRobot().apply { func() }
class AllShowsRobot {
    fun verifyAllShows() {
        onView(withText(R.string.shows)).check(matches(isDisplayed()))
        onView(withId(R.id.shows))
            .check(matches(MatcherUtils.withListSize(1)))
    }
}