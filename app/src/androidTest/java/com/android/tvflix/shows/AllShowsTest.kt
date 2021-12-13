package com.android.tvflix.shows

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.android.tvflix.idlingresource.LoadingIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AllShowsTest {
    // @get:Rule
    // val homeActivityTestRule = ActivityTestRule(HomeActivity::class.java)
    @get:Rule
    val allShowsActivityTestRule = ActivityTestRule(AllShowsActivity::class.java)

    private lateinit var loadingIdlingResource: LoadingIdlingResource
    @Before
    fun setUp() {
        loadingIdlingResource =
            LoadingIdlingResource(allShowsActivityTestRule.activity)
        IdlingRegistry.getInstance().register(loadingIdlingResource)
    }

    @Test
    fun testAllShowsViaHome() {
        launchAllShows {
            verifyAllShows()
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(loadingIdlingResource)
    }
}