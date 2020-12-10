package com.android.tvmaze.favorite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.android.tvmaze.R
import com.android.tvmaze.databinding.ActivityFavoriteShowsBinding
import com.android.tvmaze.db.favouriteshow.FavoriteShow
import com.android.tvmaze.utils.GridItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteShowsActivity : AppCompatActivity(), FavoriteShowsAdapter.Callback {
    private val favoriteShowsViewModel: FavoriteShowsViewModel by viewModels()
    private val binding by lazy { ActivityFavoriteShowsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        favoriteShowsViewModel.loadFavoriteShows()
        favoriteShowsViewModel.getFavoriteShowsLiveData()
            .observe(this, { showFavorites(it) })
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
            val layoutManager = GridLayoutManager(this, COLUMNS_COUNT)
            binding.shows.layoutManager = layoutManager
            val favoriteShowsAdapter = FavoriteShowsAdapter(favoriteShows.toMutableList(), this)
            binding.shows.adapter = favoriteShowsAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.show_grid_spacing)
            binding.shows.addItemDecoration(GridItemDecoration(spacing, COLUMNS_COUNT))
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
        private const val COLUMNS_COUNT = 2

        fun start(context: Activity) {
            val starter = Intent(context, FavoriteShowsActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
