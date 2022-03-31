package com.android.tvflix.home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.android.tvflix.analytics.Analytics
import com.android.tvflix.domain.AddToFavoritesUseCase
import com.android.tvflix.domain.GetFavoriteShowsUseCase
import com.android.tvflix.domain.GetSchedulesUseCase
import com.android.tvflix.domain.RemoveFromFavoritesUseCase
import com.android.tvflix.utils.MainCoroutineRule
import com.android.tvflix.utils.TestUtil
import com.android.tvflix.utils.runBlockingTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
@ExperimentalCoroutinesApi
class HomeViewModelTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getSchedulesUseCase: GetSchedulesUseCase

    @Mock
    private lateinit var addToFavoritesUseCase: AddToFavoritesUseCase

    @Mock
    private lateinit var removeFromFavoritesUseCase: RemoveFromFavoritesUseCase

    @Mock
    private lateinit var getFavoriteShowsUseCase: GetFavoriteShowsUseCase

    private val testDispatcher = coroutineRule.testDispatcher

    @Mock
    private lateinit var analytics: Analytics

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test if home is loaded with shows and without favorites`() {
        coroutineRule.runBlockingTest {
            // Stubbing network calls with fake episode list
            whenever(getSchedulesUseCase.invoke(Unit))
                .thenReturn(TestUtil.getFakeEpisodeList())
            // Stub repository with empty list
            whenever(getFavoriteShowsUseCase.invoke(Unit))
                .thenReturn(emptyList())

            val homeViewModel = createHomeViewModel()
            homeViewModel.onScreenCreated()

            // Observe on `first` terminal operator for homeViewStateFlow
            val homeState = homeViewModel.homeViewStateFlow.first() as HomeViewState.Success
            assertThat(homeState).isNotNull()
            val episodes = homeState.homeViewData.episodes
            assertThat(episodes.isNotEmpty()).isTrue()
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
            context,
            getSchedulesUseCase,
            getFavoriteShowsUseCase,
            addToFavoritesUseCase,
            removeFromFavoritesUseCase,
            testDispatcher,
            analytics
        )
    }

    @Test
    fun `test if home is loaded with shows and favorites`() {
        coroutineRule.runBlockingTest {
            // Stubbing network calls with fake episode list
            whenever(getSchedulesUseCase.invoke(Unit))
                .thenReturn(TestUtil.getFakeEpisodeList())
            // Stub repository with fake favorites
            whenever(getFavoriteShowsUseCase.invoke(Unit))
                .thenReturn(arrayListOf(1, 2))
            val homeViewModel = createHomeViewModel()
            homeViewModel.onScreenCreated()
            // Observe on home view state for the first item emitted by flow
            val homeState = homeViewModel.homeViewStateFlow.first() as HomeViewState.Success
            assertThat(homeState).isNotNull()
            val episodes = homeState.homeViewData.episodes
            assertThat(episodes.isNotEmpty()).isTrue()
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
            whenever(getSchedulesUseCase.invoke(Unit))
                .thenThrow(RuntimeException("Error occurred"))
            // Stub repository with fake favorites
            whenever(getFavoriteShowsUseCase.invoke(Unit))
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