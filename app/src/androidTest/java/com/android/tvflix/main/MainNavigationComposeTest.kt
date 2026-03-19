package com.android.tvflix.main

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainNavigationComposeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appLaunchesAndReachesHomeShell() {
        waitForHomeShell()
        composeRule.onNodeWithTag("nav_shows").assertExists()
    }

    @Test
    fun navigateToShowsRouteFromHome() {
        waitForHomeShell()
        composeRule.onNodeWithTag("nav_shows").performClick()
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithTag("shows_list").fetchSemanticsNodes().isNotEmpty() ||
                composeRule.onAllNodesWithTag("retry").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun navigateToFavoritesRouteWhenFeatureEnabled() {
        waitForHomeShell()
        val favoritesNodeCount = composeRule.onAllNodesWithTag("nav_favorites")
            .fetchSemanticsNodes().size
        if (favoritesNodeCount > 0) {
            composeRule.onNodeWithTag("nav_favorites").performClick()
            composeRule.waitUntil(10_000) {
                composeRule.onAllNodesWithTag("favorites_list").fetchSemanticsNodes().isNotEmpty() ||
                    composeRule.onAllNodesWithTag("retry").fetchSemanticsNodes().isNotEmpty()
            }
        }
    }

    private fun waitForHomeShell() {
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithTag("nav_shows").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
