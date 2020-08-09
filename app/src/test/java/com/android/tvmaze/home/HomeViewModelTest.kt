package com.android.tvmaze.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.tvmaze.favorite.FavoriteShowsRepository
import com.android.tvmaze.network.TvMazeApi
import com.android.tvmaze.utils.LiveDataTestUtil
import com.android.tvmaze.utils.MainCoroutineRule
import com.android.tvmaze.utils.TestUtil
import com.android.tvmaze.utils.runBlockingTest
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
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
        mainCoroutineRule.testDispatcher.runBlockingTest {
            // Stubbing network calls with fake episode list
            whenever(tvMazeApi.getCurrentSchedule("US", TestUtil.currentDate))
                .thenReturn(TestUtil.getFakeEpisodeList())
            // Stub repository with empty list
            whenever(favoriteShowsRepository.allFavoriteShowIds())
                .thenReturn(emptyList())

            homeViewModel.onScreenCreated()

            // Observe on home view state live data
            LiveDataTestUtil.getValue(homeViewModel.getHomeViewState()) {
                when (it) {
                    is Loading -> assertThat(it).isNotNull()
                    is Success -> {
                        assertThat(it.homeViewData).isNotNull()
                        val episodes = it.homeViewData.episodes
                        assertThat(episodes.isNotEmpty())
                        // compare the response with fake list
                        assertThat(episodes).hasSize(TestUtil.getFakeEpisodeList().size)
                        // compare the data and also order
                        assertThat(episodes).containsExactlyElementsIn(
                            TestUtil.getFakeEpisodeViewDataList(
                                false
                            )
                        ).inOrder()
                    }
                }
            }
        }
    }

    @Test
    fun testHomeIsLoadedWithShowsAndFavorites() {
        mainCoroutineRule.runBlockingTest {
            // Stubbing network calls with fake episode list
            whenever(tvMazeApi.getCurrentSchedule("US", TestUtil.currentDate))
                .thenReturn(TestUtil.getFakeEpisodeList())
            // Stub repository with fake favorites
            whenever(favoriteShowsRepository.allFavoriteShowIds())
                .thenReturn(arrayListOf(1, 2))

            homeViewModel.onScreenCreated()
            // Observe on home view state live data
            LiveDataTestUtil.getValue(homeViewModel.getHomeViewState()) {
                when (it) {
                    is Loading -> assertThat(it).isNotNull()
                    is Success -> {
                        assertThat(it.homeViewData).isNotNull()
                        val episodes = it.homeViewData.episodes
                        assertThat(episodes.isNotEmpty())
                        // compare the response with fake list
                        assertThat(episodes).hasSize(TestUtil.getFakeEpisodeList().size)
                        // compare the data and also order
                        assertThat(episodes).containsExactlyElementsIn(
                            TestUtil.getFakeEpisodeViewDataList(
                                true
                            )
                        ).inOrder()
                    }
                }
            }
        }
    }

    @Test
    fun testNetworkError() {
        mainCoroutineRule.runBlockingTest {
            // Stubbing network calls with fake episode list
            whenever(tvMazeApi.getCurrentSchedule("US", TestUtil.currentDate))
                .thenReturn(null)
            // Stub repository with fake favorites
            whenever(favoriteShowsRepository.allFavoriteShowIds())
                .thenReturn(arrayListOf(1, 2))

            homeViewModel.onScreenCreated()
            LiveDataTestUtil.getValue(homeViewModel.getHomeViewState()) {
                when (it) {
                    is Loading -> assertThat(it).isNotNull()
                    is NetworkError -> assertThat(it.message).isNotEmpty()
                }
            }
        }
    }

    @After
    fun release() {
        Mockito.framework().clearInlineMocks()
    }
}