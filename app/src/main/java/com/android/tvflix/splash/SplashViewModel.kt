package com.android.tvflix.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tvflix.config.AppConfig
import com.android.tvflix.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    private val appConfig: AppConfig,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _splashViewStateFlow = MutableStateFlow<SplashViewState>(SplashViewState.Idle)

    // Represents _splashViewStateFlow mutable state flow as a read-only state flow.
    val splashViewStateFlow = _splashViewStateFlow.asStateFlow()
    fun fetchConfig() {
        viewModelScope.launch(dispatcher) {
            appConfig.fetch()
            _splashViewStateFlow.emit(SplashViewState.NavigateToHome)
        }
    }
}