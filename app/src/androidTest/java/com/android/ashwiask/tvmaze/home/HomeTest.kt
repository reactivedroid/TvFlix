package com.android.ashwiask.tvmaze.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.android.ashwiask.tvmaze.robots.launchHome
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeTest {
    @get:Rule
    val homeActivityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testHomePage() {
        launchHome {
            verifyHome()
            verifyFavorite()
            verifyToast(homeActivityRule.activity)
        }
    }
}