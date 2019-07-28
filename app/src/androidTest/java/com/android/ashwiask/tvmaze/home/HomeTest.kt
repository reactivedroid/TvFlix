package com.android.ashwiask.tvmaze.home

import androidx.test.espresso.Espresso.pressBack
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
    val activityTestRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testHomePageWithFavorites() {
        launchHome {
            verifyHome()
            // Click on add to favorites icon
            verifyFavorite()
            //Verify that added to favorites toast is shown
            verifyToast(activityTestRule.activity)
            verifyFavoriteScreen()
            // Verify that pressing back from favorites goes to home
            pressBack()
            verifyHome()
            // Verify All shows screen
            verifyAllShows()
            // Verify that pressing back from all shows screen goes to home
            pressBack()
            verifyHome()
        }
    }
}