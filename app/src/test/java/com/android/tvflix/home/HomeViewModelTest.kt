package com.android.tvflix.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.tvflix.analytics.Analytics
import com.android.tvflix.favorite.FavoriteShowsRepository
import com.android.tvflix.network.TvFlixApi
import com.android.tvflix.utils.MainCoroutineRule
import com.android.tvflix.utils.TestUtil
import com.android.tvflix.utils.runBlockingTest
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var tvFlixApi: TvFlixApi

    @Mock
    private lateinit var favoriteShowsRepository: FavoriteShowsRepository

    private val testDispatcher = coroutineRule.testDispatcher

    @Mock
    private lateinit var analytics: Analytics

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `test if home is loaded with shows and without favorites`() {
        coroutineRule.runBlockingTest {
            // Stubbing network calls with fake episode list
            whenever(tvFlixApi.getCurrentSchedule("US", TestUtil.currentDate))
                .thenReturn(TestUtil.getFakeEpisodeList())
            // Stub repository with empty list
            whenever(favoriteShowsRepository.allFavoriteShowIds())
                .thenReturn(emptyList())

            val homeViewModel = createHomeViewModel()
            homeViewModel.onScreenCreated()

            // Observe on `first` terminal operator for homeViewStateFlow
            val homeState = homeViewModel.homeViewStateFlow.first() as HomeViewState.Success
            assertThat(homeState).isNotNull()
            val episodes = homeState.homeViewData.episodes
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

    private fun createHomeViewModel(): HomeViewModel {
        return HomeViewModel(
            tvFlixApi = tvFlixApi,
            favoriteShowsRepository = favoriteShowsRepository,
            dispatcher = testDispatcher,
            analytics = analytics
        )
    }

    @Test
    fun `test if home is loaded with shows and favorites`() {
        coroutineRule.runBlockingTest {
            // Stubbing network calls with fake episode list
            whenever(tvFlixApi.getCurrentSchedule("US", TestUtil.currentDate))
                .thenReturn(TestUtil.getFakeEpisodeList())
            // Stub repository with fake favorites
            whenever(favoriteShowsRepository.allFavoriteShowIds())
                .thenReturn(arrayListOf(1, 2))
            val homeViewModel = createHomeViewModel()
            homeViewModel.onScreenCreated()
            // Observe on home view state for the first item emitted by flow
            val homeState = homeViewModel.homeViewStateFlow.first() as HomeViewState.Success
            assertThat(homeState).isNotNull()
            val episodes = homeState.homeViewData.episodes
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

    @Test
    fun `test for failure`() {
        coroutineRule.runBlockingTest {
            val homeViewModel = createHomeViewModel()
            // Stubbing network calls with fake episode list
            whenever(tvFlixApi.getCurrentSchedule("US", TestUtil.currentDate))
                .thenThrow(RuntimeException("Error occurred"))
            // Stub repository with fake favorites
            whenever(favoriteShowsRepository.allFavoriteShowIds())
                .thenReturn(arrayListOf(1, 2))

            homeViewModel.onScreenCreated()
            val homeState = homeViewModel.homeViewStateFlow.first() as HomeViewState.NetworkError
            assertThat(homeState.message).isNotNull()
            assertThat(homeState.message).isEqualTo("Error occurred")
        }
    }

    @After
    fun release() {
        Mockito.framework().clearInlineMocks()
    }
}