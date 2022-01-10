package com.android.tvflix.splash

sealed class SplashViewState {
    object Idle : SplashViewState()
    object NavigateToHome : SplashViewState()
}