package com.android.tvflix.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.tvflix.network.home.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ShowsViewModel @Inject
constructor(
    private val showsRepository: ShowsRepository
) : ViewModel() {
    fun shows(): Flow<PagingData<Show>> {
        return showsRepository.getShows()
            .cachedIn(viewModelScope)
    }
}
