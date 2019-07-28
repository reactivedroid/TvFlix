package com.android.ashwiask.tvmaze.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.ashwiask.tvmaze.LiveDataTestUtil
import com.android.ashwiask.tvmaze.MainCoroutineRule
import com.android.ashwiask.tvmaze.TestUtil
import com.android.ashwiask.tvmaze.favorite.FavoriteShowsRepository
import com.android.ashwiask.tvmaze.network.TvMazeApi
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {
    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var tvMazeApi: TvMazeApi
    @Mock
    private lateinit var favoriteShowsRepository: FavoriteShowsRepository
    @InjectMocks
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testHomeIsLoadedWithShowsWithoutFavorites() {
        runBlocking {
            // Stubbing network calls with fake episode list
            whenever(tvMazeApi.getCurrentSchedule("US", TestUtil.currentDate))
                .thenReturn(TestUtil.getFakeEpisodeList())
            // Stub repository with empty list
            whenever(favoriteShowsRepository.allFavoriteShowIds())
                .thenReturn(emptyList())

            // Pause coroutine to listen for loading initial state
            mainCoroutineRule.pauseDispatcher {
                homeViewModel.onScreenCreated()
            }
            // Check if status is loading
            assertThat(LiveDataTestUtil.getValue(homeViewModel.getHomeViewState())).isEqualTo(Loading)
            // Resume coroutine dispatcher to execute pending coroutine actions
            mainCoroutineRule.resumeDispatcher()
            // Delay the coroutine for the job to finish
            delay(1_00)
            // Observe on home view state live data
            val homeViewState = LiveDataTestUtil.getValue(homeViewModel.getHomeViewState())
            // Check for success data
            assertThat(homeViewState is Success).isTrue()
            val homeViewData = (homeViewState as Success).homeViewData
            assertThat(homeViewData.episodes).isNotEmpty()
            // compare the response with fake list
            assertThat(homeViewData.episodes).hasSize(TestUtil.getFakeEpisodeList().size)
            // compare the data and also order
            assertThat(homeViewData.episodes).containsExactlyElementsIn(
                TestUtil.getFakeEpisodeViewDataList(
                    false
                )
            ).inOrder()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testHomeIsLoadedWithShowsAndFavorites() {
        runBlocking {
            // Stubbing network calls with fake episode list
            whenever(tvMazeApi.getCurrentSchedule("US", TestUtil.currentDate))
                .thenReturn(TestUtil.getFakeEpisodeList())
            // Stub repository with fake favorites
            whenever(favoriteShowsRepository.allFavoriteShowIds())
                .thenReturn(arrayListOf(1, 2))

            // Pause coroutine to listen for loading initial state
            mainCoroutineRule.pauseDispatcher {
                homeViewModel.onScreenCreated()
            }
            // Check if status is loading
            assertThat(LiveDataTestUtil.getValue(homeViewModel.getHomeViewState())).isEqualTo(Loading)
            // Resume coroutine dispatcher to execute pending coroutine actions
            mainCoroutineRule.resumeDispatcher()

            // Delay the coroutine for the job to finish
            delay(1_00)

            // Observe on home view state live data
            val homeViewState = LiveDataTestUtil.getValue(homeViewModel.getHomeViewState())
            // Check for success data
            assertThat(homeViewState is Success).isTrue()
            val homeViewData = (homeViewState as Success).homeViewData
            assertThat(homeViewData.episodes).isNotEmpty()
            // compare the response with fake list
            assertThat(homeViewData.episodes).hasSize(TestUtil.getFakeEpisodeList().size)
            // compare the data and also order
            assertThat(homeViewData.episodes).containsExactlyElementsIn(
                TestUtil.getFakeEpisodeViewDataList(
                    true
                )
            ).inOrder()
        }
    }
}