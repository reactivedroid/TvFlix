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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.android.ashwiask.tvmaze.R
import com.android.ashwiask.tvmaze.base.TvMazeBaseActivity
import com.android.ashwiask.tvmaze.common.Constants
import com.android.ashwiask.tvmaze.common.GridItemDecoration
import com.android.ashwiask.tvmaze.databinding.ActivityFavoriteShowsBinding
import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow
import javax.inject.Inject

class FavoriteShowsActivity : TvMazeBaseActivity(), FavoriteShowsAdapter.Callback {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var favoriteShowsViewModel: FavoriteShowsViewModel
    private lateinit var binding: ActivityFavoriteShowsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_shows)
        setToolbar()
        favoriteShowsViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FavoriteShowsViewModel::class.java)
        favoriteShowsViewModel.loadFavoriteShows()
        favoriteShowsViewModel.getFavoriteShowsLiveData()
            .observe(this, Observer { showFavorites(it) })
    }

    private fun setToolbar() {
        val toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        supportActionBar?.run { setDisplayHomeAsUpEnabled(true) }
        setTitle(R.string.favorite_shows)
    }


    private fun showFavorites(favoriteShows: List<FavoriteShow>) {
        binding.progress.visibility = View.GONE
        if (favoriteShows.isNotEmpty()) {
            val layoutManager = GridLayoutManager(this, Constants.NO_OF_COLUMNS)
            binding.shows.layoutManager = layoutManager
            val favoriteShowsAdapter = FavoriteShowsAdapter(favoriteShows.toMutableList(), this)
            binding.shows.adapter = favoriteShowsAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.show_grid_spacing)
            binding.shows.addItemDecoration(GridItemDecoration(spacing, Constants.NO_OF_COLUMNS))
            binding.shows.visibility = View.VISIBLE
        } else {
            val bookmarkSpan = ImageSpan(this, R.drawable.favorite_border)
            val spannableString = SpannableString(getString(R.string.favorite_hint_msg))
            spannableString.setSpan(
                bookmarkSpan, FAVORITE_ICON_START_OFFSET,
                FAVORITE_ICON_END_OFFSET, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.favoriteHint.text = spannableString
            binding.favoriteHint.visibility = View.VISIBLE
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

        fun startForResult(context: Activity, requestCode: Int) {
            val starter = Intent(context, FavoriteShowsActivity::class.java)
            context.startActivityForResult(starter, requestCode)
        }
    }
}
