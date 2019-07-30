package com.android.ashwiask.tvmaze.favorites

import android.content.Context
import androidx.room.Room
import com.android.ashwiask.tvmaze.db.TvMazeDatabase
import com.android.ashwiask.tvmaze.db.favouriteshow.ShowDao
import com.android.ashwiask.tvmaze.favorite.FavoriteShowsRepository
import com.android.ashwiask.tvmaze.utils.TestUtil
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class FavoriteShowsRepositoryTest {
    @Mock
    private lateinit var context: Context
    private lateinit var favoriteShowsRepository: FavoriteShowsRepository
    private lateinit var tvMazeDb: TvMazeDatabase
    private lateinit var showDao: ShowDao

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        tvMazeDb = Room.inMemoryDatabaseBuilder(
            context, TvMazeDatabase::class.java
        ).build()
        showDao = tvMazeDb.showDao()
        favoriteShowsRepository = FavoriteShowsRepository(showDao)
    }

    @Test
    fun testShowInsertionInDb() {
        runBlocking {
            favoriteShowsRepository.insertIntoFavorites(TestUtil.getFakeShow())
            val favShows = favoriteShowsRepository.allFavoriteShows()
            assertThat(favShows.isNotEmpty()).isTrue()
        }
    }

    @Test
    fun testRemoveFromDb() {
        runBlocking {
            favoriteShowsRepository.clearAll()
            assertThat(favoriteShowsRepository.allFavoriteShows().isEmpty()).isTrue()
        }
    }

    @Test
    fun testFavoriteShows() {
        runBlocking {
            val fakeShow = TestUtil.getFakeShow()
            favoriteShowsRepository.insertIntoFavorites(fakeShow)
            val favoriteShows = favoriteShowsRepository.allFavoriteShows()
            assertThat(favoriteShows[0] == fakeShow).isTrue()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        tvMazeDb.close()
    }
}