package com.android.tvmaze.shows

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.tvmaze.network.home.Show
import kotlinx.coroutines.flow.Flow

class ShowsViewModel @ViewModelInject
constructor(
    private val showsRepository: ShowsRepository
) : ViewModel() {
    fun shows(): Flow<PagingData<Show>> {
        return showsRepository.getShows()
            .cachedIn(viewModelScope)
    }
}
