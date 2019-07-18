package com.android.ashwiask.tvmaze.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.android.ashwiask.tvmaze.common.GridItemDecoration
import com.android.ashwiask.tvmaze.databinding.ActivityHomeBinding
import com.android.ashwiask.tvmaze.favorite.FavoriteShowsActivity
import com.android.ashwiask.tvmaze.shows.AllShowsActivity
import javax.inject.Inject

class HomeActivity : TvMazeBaseActivity(), ShowsAdapter.Callback {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var showsAdapter: ShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        setToolbar()
        homeViewModel.onScreenCreated()
        homeViewModel.isLoading().observe(this, Observer { setProgress(it) })
        homeViewModel.getPopularShowsList().observe(this, Observer { showPopularShows(it) })
        homeViewModel.getErrorMsg().observe(this, Observer { showError(it) })
        binding.popularShowHeader.text = String.format(
            getString(R.string.popular_shows_airing_today),
            homeViewModel.country
        )
    }

    private fun setToolbar() {
        val toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setTitle(R.string.app_name)
    }

    private fun setProgress(isLoading: Boolean) {
        if (isLoading) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    private fun showPopularShows(homeViewData: HomeViewData) {
        val layoutManager = GridLayoutManager(this, NO_OF_COLUMNS)
        binding.popularShows.layoutManager = layoutManager
        binding.popularShows.setHasFixedSize(true)
        showsAdapter = ShowsAdapter(this)
        showsAdapter.updateList(homeViewData.episodes.toMutableList())
        binding.popularShows.adapter = showsAdapter
        val spacing = resources.getDimensionPixelSize(R.dimen.show_grid_spacing)
        binding.popularShows.addItemDecoration(GridItemDecoration(spacing, NO_OF_COLUMNS))
        binding.popularShows.isNestedScrollingEnabled = false
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_shows -> {
                AllShowsActivity.start(this)
                true
            }
            R.id.action_favorites -> {
                FavoriteShowsActivity.startForResult(this, REQUEST_CODE_FAVORITE_SHOWS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onFavoriteClicked(showViewData: HomeViewData.ShowViewData) {
        if (!showViewData.isFavoriteShow) {
            homeViewModel.addToFavorite(showViewData.show)
            Toast.makeText(this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show()
        } else {
            homeViewModel.removeFromFavorite(showViewData.show)
            Toast.makeText(this, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val NO_OF_COLUMNS = 2
        private const val REQUEST_CODE_FAVORITE_SHOWS = 11
    }
}
