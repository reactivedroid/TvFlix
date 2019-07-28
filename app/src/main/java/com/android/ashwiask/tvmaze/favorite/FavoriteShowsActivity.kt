package com.android.ashwiask.tvmaze.favorite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.android.ashwiask.tvmaze.R
import com.android.ashwiask.tvmaze.base.TvMazeBaseActivity
import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow
import com.android.ashwiask.tvmaze.utils.GridItemDecoration
import kotlinx.android.synthetic.main.activity_favorite_shows.*
import kotlinx.android.synthetic.main.toolbar.view.*
import javax.inject.Inject

class FavoriteShowsActivity : TvMazeBaseActivity(), FavoriteShowsAdapter.Callback {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var favoriteShowsViewModel: FavoriteShowsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_shows)
        setToolbar()
        favoriteShowsViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FavoriteShowsViewModel::class.java)
        favoriteShowsViewModel.loadFavoriteShows()
        favoriteShowsViewModel.getFavoriteShowsLiveData()
            .observe(this, Observer { showFavorites(it) })
    }

    private fun setToolbar() {
        val toolbar = toolbar.toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        supportActionBar?.run { setDisplayHomeAsUpEnabled(true) }
        setTitle(R.string.favorite_shows)
    }


    private fun showFavorites(favoriteShows: List<FavoriteShow>) {
        progress.visibility = View.GONE
        if (favoriteShows.isNotEmpty()) {
            val layoutManager = GridLayoutManager(this, COLUMNS_COUNT)
            shows.layoutManager = layoutManager
            val favoriteShowsAdapter = FavoriteShowsAdapter(favoriteShows.toMutableList(), this)
            shows.adapter = favoriteShowsAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.show_grid_spacing)
           shows.addItemDecoration(GridItemDecoration(spacing, COLUMNS_COUNT))
            shows.visibility = View.VISIBLE
        } else {
            val bookmarkSpan = ImageSpan(this, R.drawable.favorite_border)
            val spannableString = SpannableString(getString(R.string.favorite_hint_msg))
            spannableString.setSpan(
                bookmarkSpan, FAVORITE_ICON_START_OFFSET,
                FAVORITE_ICON_END_OFFSET, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            favorite_hint.text = spannableString
            favorite_hint.visibility = View.VISIBLE
        }
    }

    override fun onFavoriteClicked(show: FavoriteShow) {
        if (!show.isFavorite) {
            favoriteShowsViewModel.addToFavorite(show)
            Toast.makeText(this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show()
        } else {
            favoriteShowsViewModel.removeFromFavorite(show)
            Toast.makeText(this, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val FAVORITE_ICON_START_OFFSET = 13
        private const val FAVORITE_ICON_END_OFFSET = 14
        private const val COLUMNS_COUNT = 2

        fun start(context: Activity) {
            val starter = Intent(context, FavoriteShowsActivity::class.java)
            context.startActivity(starter)
        }
    }
}
