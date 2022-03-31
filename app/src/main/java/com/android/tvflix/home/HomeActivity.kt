package com.android.tvflix.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.tvflix.R
import com.android.tvflix.config.FavoritesFeatureFlag
import com.android.tvflix.databinding.ActivityHomeBinding
import com.android.tvflix.domain.GetSchedulesUseCase
import com.android.tvflix.favorite.FavoriteShowsActivity
import com.android.tvflix.shows.AllShowsActivity
import com.android.tvflix.utils.GridItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), ShowsAdapter.Callback {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var showsAdapter: ShowsAdapter
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    @JvmField
    @FavoritesFeatureFlag
    @Inject
    var favoritesFeatureEnable: Boolean = false

    companion object {
        private const val NO_OF_COLUMNS = 2

        @JvmStatic
        fun start(
            context: Activity
        ) {
            val intent = Intent(context, HomeActivity::class.java)
                .apply {
                    flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        homeViewModel.onScreenCreated()
        lifecycleScope.launchWhenStarted {
            homeViewModel.homeViewStateFlow.collect { setViewState(it) }
        }
    }

    private fun setViewState(homeViewState: HomeViewState) {
        when (homeViewState) {
            is HomeViewState.Loading -> binding.progress.isVisible = true
            is HomeViewState.NetworkError -> {
                binding.progress.isVisible = false
                showError(homeViewState.message!!)
            }
            is HomeViewState.Success -> {
                with(binding) {
                    progress.isVisible = false
                    popularShowHeader.text = homeViewState.homeViewData.heading
                    popularShowHeader.isVisible = true
                }
                showPopularShows(homeViewState.homeViewData)
            }
            is HomeViewState.AddedToFavorites ->
                Toast.makeText(
                    this,
                    getString(R.string.added_to_favorites, homeViewState.show.name),
                    Toast.LENGTH_SHORT
                ).show()
            is HomeViewState.RemovedFromFavorites ->
                Toast.makeText(
                    this,
                    getString(R.string.removed_from_favorites, homeViewState.show.name),
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun setToolbar() {
        val toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setTitle(R.string.app_name)
    }

    private fun showPopularShows(homeViewData: HomeViewData) {
        val gridLayoutManager = GridLayoutManager(this, NO_OF_COLUMNS)
        showsAdapter = ShowsAdapter(this, favoritesFeatureEnable)
        showsAdapter.updateList(homeViewData.episodes.toMutableList())
        binding.popularShows.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = showsAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.show_grid_spacing)
            addItemDecoration(GridItemDecoration(spacing, NO_OF_COLUMNS))
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        menu.findItem(R.id.action_favorites).isVisible = favoritesFeatureEnable
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_shows -> {
                AllShowsActivity.start(this)
                true
            }
            R.id.action_favorites -> {
                FavoriteShowsActivity.start(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onFavoriteClicked(showViewData: HomeViewData.ShowViewData) {
        homeViewModel.onFavoriteClick(showViewData)
    }
}
