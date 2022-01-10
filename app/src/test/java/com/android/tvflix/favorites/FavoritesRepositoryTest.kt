package com.android.tvflix.favorites

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.android.tvflix.db.TvFlixDatabase
import com.android.tvflix.db.favouriteshow.ShowDao
import com.android.tvflix.favorite.FavoriteShowsRepository
import com.android.tvflix.utils.TestUtil
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoritesRepositoryTest {
    private lateinit var favoriteShowsRepository: FavoriteShowsRepository
    private lateinit var tvFlixDb: TvFlixDatabase
    private lateinit var showDao: ShowDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        tvFlixDb = Room.inMemoryDatabaseBuilder(
            context, TvFlixDatabase::class.java
        ).allowMainThreadQueries().build()
        showDao = tvFlixDb.showDao()
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
    fun release() {
        tvFlixDb.close()
    }
}