package com.android.tvmaze.favorite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.tvmaze.R
import com.android.tvmaze.databinding.ActivityFavoriteShowsBinding
import com.android.tvmaze.db.favouriteshow.FavoriteShow
import com.android.tvmaze.utils.GridItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FavoriteShowsActivity : AppCompatActivity(), FavoriteShowsAdapter.Callback {
    private val favoriteShowsViewModel: FavoriteShowsViewModel by viewModels()
    private val binding by lazy { ActivityFavoriteShowsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        favoriteShowsViewModel.loadFavoriteShows()
        lifecycleScope.launchWhenStarted {
            favoriteShowsViewModel.favoriteShowsStateFlow.collect { setViewState(it) }
        }
    }

    private fun setViewState(favoriteShowState: FavoriteShowState) {
        when (favoriteShowState) {
            is FavoriteShowState.Loading -> binding.progress.isVisible = true
            is FavoriteShowState.AllFavorites ->
                showFavorites(favoriteShowState.favoriteShows)
            is FavoriteShowState.Error, FavoriteShowState.Empty -> showEmptyState()
            is FavoriteShowState.AddedToFavorites ->
                Toast.makeText(this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show()
            is FavoriteShowState.RemovedFromFavorites ->
                Toast.makeText(this, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show()
        }
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
        binding.progress.isVisible = false
        val layoutManager = GridLayoutManager(this, COLUMNS_COUNT)
        binding.shows.layoutManager = layoutManager
        val favoriteShowsAdapter = FavoriteShowsAdapter(favoriteShows.toMutableList(), this)
        binding.shows.adapter = favoriteShowsAdapter
        val spacing = resources.getDimensionPixelSize(R.dimen.show_grid_spacing)
        binding.shows.addItemDecoration(GridItemDecoration(spacing, COLUMNS_COUNT))
        binding.shows.isVisible = true
    }

    private fun showEmptyState() {
        binding.progress.isVisible = false
        val bookmarkSpan = ImageSpan(this, R.drawable.favorite_border)
        val spannableString = SpannableString(getString(R.string.favorite_hint_msg))
        spannableString.setSpan(
            bookmarkSpan, FAVORITE_ICON_START_OFFSET,
            FAVORITE_ICON_END_OFFSET, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.favoriteHint.text = spannableString
        binding.favoriteHint.isVisible = true
    }

    override fun onFavoriteClicked(show: FavoriteShow) {
        favoriteShowsViewModel.onFavoriteClick(show)
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
