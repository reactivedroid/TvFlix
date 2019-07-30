package com.android.ashwiask.tvmaze.home

import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.android.ashwiask.tvmaze.idlingresource.LoadingIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeTest {
    @get:Rule
    val homeActivityTestRule = ActivityTestRule(HomeActivity::class.java)
    private lateinit var loadingIdlingResource: LoadingIdlingResource

    @Before
    fun setUp() {
        loadingIdlingResource =
            LoadingIdlingResource(homeActivityTestRule.activity)
        IdlingRegistry.getInstance().register(loadingIdlingResource)
    }

    @Test
    fun testHomePageWithFavorites() {
        launchHome {
            verifyHome()
            // Click on add to favorites icon
            verifyFavorite()
            // Verify that added to favorites toast is shown
            verifyToast(homeActivityTestRule.activity)
            verifyFavoriteScreen()
            // Verify that pressing back from favorites goes to home
            pressBack()
            verifyHome()
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(loadingIdlingResource)
    }
}