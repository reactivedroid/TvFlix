package com.android.tvmaze.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.android.tvmaze.network.TvMazeApi
import com.android.tvmaze.network.home.Show
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowsDataSource @Inject
constructor(
    private val tvMazeApi: TvMazeApi
) : ItemKeyedDataSource<Int, Show>() {
    private var pageNumber = 1
    private val paginatedNetworkStateLiveData: MutableLiveData<NetworkState> = MutableLiveData()
    private val initialLoadStateLiveData: MutableLiveData<NetworkState> = MutableLiveData()
    // For Retry
    private lateinit var params: LoadParams<Int>
    private lateinit var callback: LoadCallback<Show>
    private lateinit var job: Job

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Show>
    ) {
        Timber.d("Fetching first page: $pageNumber")
        initialLoadStateLiveData.postValue(Loading)
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            initialLoadStateLiveData.postValue(NetworkError(exception.message))
            Timber.e(exception)
        }
        job = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val shows = tvMazeApi.getShows(pageNumber)
            withContext(Dispatchers.Main) {
                onShowsFetched(shows, callback)
            }
        }
    }

    private fun onShowsFetched(
        shows: List<Show>,
        callback: LoadInitialCallback<Show>
    ) {
        initialLoadStateLiveData.postValue(Success)
        pageNumber++
        callback.onResult(shows)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Show>
    ) {
        this.params = params
        this.callback = callback
        Timber.d("Fetching next page: $pageNumber")
        paginatedNetworkStateLiveData.postValue(Loading)
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            paginatedNetworkStateLiveData.postValue(NetworkError(exception.message))
            Timber.e(exception)
        }
        job = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val shows = tvMazeApi.getShows(params.key)
            withContext(Dispatchers.Main) {
                onMoreShowsFetched(shows, callback)
            }
        }
    }

    private fun onMoreShowsFetched(shows: List<Show>, callback: LoadCallback<Show>) {
        paginatedNetworkStateLiveData.postValue(Success)
        pageNumber++
        callback.onResult(shows)
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Show>
    ) {
        // Do nothing, since data is loaded from our initial load itself
    }

    override fun getKey(item: Show): Int {
        return pageNumber
    }

    fun clear() {
        pageNumber = 1
        job.cancel()
    }

    fun getPaginatedNetworkStateLiveData(): LiveData<NetworkState> {
        return paginatedNetworkStateLiveData
    }

    fun getInitialLoadStateLiveData(): LiveData<NetworkState> {
        return initialLoadStateLiveData
    }

    fun retryPagination() {
        loadAfter(params, callback)
    }
}
