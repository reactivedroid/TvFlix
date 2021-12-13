package com.android.tvflix.shows

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.android.tvflix.R
import com.android.tvflix.databinding.ActivityAllShowsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllShowsActivity : AppCompatActivity() {
    private val showsViewModel: ShowsViewModel by viewModels()
    private lateinit var adapter: ShowsPagedAdapter
    private lateinit var showsBinding: ActivityAllShowsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showsBinding = ActivityAllShowsBinding.inflate(LayoutInflater.from(this))
        setContentView(showsBinding.root)
        setToolbar()
        initAdapter()
        getShows()
        showsBinding.retry.setOnClickListener { adapter.retry() }
    }

    private fun getShows() {
        lifecycleScope.launchWhenStarted { showsViewModel.shows().collectLatest { adapter.submitData(it) } }
    }

    private fun initAdapter() {
        adapter = ShowsPagedAdapter(ShowDiffUtilItemCallback())
        showsBinding.shows.adapter = adapter.withLoadStateFooter(
            footer = ShowsLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { combinedLoadStates ->
            // Handle the initial load state
            when (val loadState = combinedLoadStates.source.refresh) {
                is LoadState.NotLoading -> {
                    showsBinding.progress.isVisible = false
                    showsBinding.shows.isVisible = true
                    showsBinding.errorGroup.isVisible = false
                }
                is LoadState.Loading -> {
                    showsBinding.progress.isVisible = true
                    showsBinding.errorGroup.isVisible = false
                }
                is LoadState.Error -> {
                    showsBinding.progress.isVisible = false
                    showsBinding.errorGroup.isVisible = true
                    showsBinding.errorMsg.text = loadState.error.localizedMessage
                }
            }

            // Show message to the user when an error comes while loading the next page
            val errorState = combinedLoadStates.source.append as? LoadState.Error
                ?: combinedLoadStates.append as? LoadState.Error
            errorState?.let {
                Toast.makeText(this, errorState.error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setToolbar() {
        val toolbar = showsBinding.toolbar.toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.shows)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, AllShowsActivity::class.java)
            context.startActivity(starter)
        }
    }
}
