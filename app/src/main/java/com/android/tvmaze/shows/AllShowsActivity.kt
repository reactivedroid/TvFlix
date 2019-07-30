package com.android.tvmaze.shows

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.tvmaze.R
import com.android.tvmaze.base.TvMazeBaseActivity
import com.android.tvmaze.network.home.Show
import kotlinx.android.synthetic.main.activity_all_shows.*
import kotlinx.android.synthetic.main.toolbar.view.*
import javax.inject.Inject

class AllShowsActivity : TvMazeBaseActivity(), ShowsPagedAdaptor.Callback {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var showsViewModel: ShowsViewModel
    private lateinit var showsPagedAdaptor: ShowsPagedAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_shows)
        setToolbar()
        showsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShowsViewModel::class.java)
        showsViewModel.onScreenCreated()
        initAdapter()
        showsViewModel.initialLoadState().observe(this, Observer { setProgress(it) })
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        showsPagedAdaptor = ShowsPagedAdaptor(ShowDiffUtilItemCallback(), this)
        shows.layoutManager = layoutManager
        shows.adapter = showsPagedAdaptor
        showsViewModel.getShows().observe(this, Observer { showAllShows(it) })
        showsViewModel.paginatedLoadState().observe(this, Observer { setAdapterState(it) })
    }

    private fun setAdapterState(networkState: NetworkState) {
        showsPagedAdaptor.setNetworkState(networkState)
    }

    private fun setToolbar() {
        val toolbar = toolbar.toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.shows)
    }

    private fun setProgress(loadState: NetworkState) {
        when (loadState) {
            is Success -> progress.visibility = View.GONE
            is NetworkError -> {
                progress.visibility = View.GONE
                Toast.makeText(this, loadState.message, Toast.LENGTH_SHORT).show()
            }
            is Loading -> progress.visibility = View.VISIBLE
        }
    }

    private fun showAllShows(showsList: PagedList<Show>) {
        showsPagedAdaptor.submitList(showsList)
        shows.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onRetryClicked() {
        showsViewModel.retry()
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, AllShowsActivity::class.java)
            context.startActivity(starter)
        }
    }
}
