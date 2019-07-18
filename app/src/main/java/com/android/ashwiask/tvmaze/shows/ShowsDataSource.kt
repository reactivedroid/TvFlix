package com.android.ashwiask.tvmaze.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.android.ashwiask.tvmaze.network.TvMazeApi
import com.android.ashwiask.tvmaze.network.home.Show
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowsDataSource @Inject
constructor(
    private val tvMazeApi: TvMazeApi
) : ItemKeyedDataSource<Int, Show>() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var pageNumber = 1
    private val paginatedNetworkStateLiveData: MutableLiveData<NetworkState> = MutableLiveData()
    private val initialLoadStateLiveData: MutableLiveData<NetworkState> = MutableLiveData()
    // For Retry
    private lateinit var params: LoadParams<Int>
    private lateinit var callback: LoadCallback<Show>

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Show>
    ) {
        Timber.d("Fetching first page: $pageNumber")
        initialLoadStateLiveData.postValue(Loading)
        val showsDisposable = tvMazeApi.getShows(pageNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onShowsFetched(it, callback) }, { onError(it) })
        compositeDisposable.add(showsDisposable)
    }

    private fun onError(throwable: Throwable) {
        initialLoadStateLiveData.postValue(NetworkError(throwable.message))
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
        val showsDisposable = tvMazeApi.getShows(params.key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onMoreShowsFetched(it, callback) },
                { this.onPaginationError(it) })
        compositeDisposable.add(showsDisposable)
    }

    private fun onMoreShowsFetched(shows: List<Show>, callback: LoadCallback<Show>) {
        paginatedNetworkStateLiveData.postValue(Success)
        pageNumber++
        callback.onResult(shows)
    }

    private fun onPaginationError(throwable: Throwable) {
        paginatedNetworkStateLiveData.postValue(NetworkError(throwable.message))
        Timber.e(throwable)
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
        compositeDisposable.clear()
        pageNumber = 1
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
