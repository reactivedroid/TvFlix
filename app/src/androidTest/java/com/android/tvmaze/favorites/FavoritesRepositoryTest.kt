package com.android.tvmaze.favorites

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.android.tvmaze.db.TvMazeDatabase
import com.android.tvmaze.db.favouriteshow.ShowDao
import com.android.tvmaze.favorite.FavoriteShowsRepository
import com.android.tvmaze.utils.TestUtils
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@LargeTest
@RunWith(AndroidJUnit4::class)
class FavoritesRepositoryTest {
    private lateinit var favoriteShowsRepository: FavoriteShowsRepository
    private lateinit var tvMazeDb: TvMazeDatabase
    private lateinit var showDao: ShowDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        tvMazeDb = Room.inMemoryDatabaseBuilder(
            context, TvMazeDatabase::class.java
        ).allowMainThreadQueries().build()
        showDao = tvMazeDb.showDao()
        favoriteShowsRepository = FavoriteShowsRepository(showDao)
    }

    @Test
    fun testShowInsertionInDb() {
        runBlocking {
            favoriteShowsRepository.insertIntoFavorites(TestUtils.getFakeShow())
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
            val fakeShow = TestUtils.getFakeShow()
            favoriteShowsRepository.insertIntoFavorites(fakeShow)
            val favoriteShows = favoriteShowsRepository.allFavoriteShows()
            assertThat(favoriteShows[0] == fakeShow).isTrue()
        }
    }

    @After
    @Throws(IOException::class)
    fun release() {
        tvMazeDb.close()
    }
}