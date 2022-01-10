package com.android.tvflix.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.tvflix.R
import com.android.tvflix.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel.fetchConfig()
        lifecycleScope.launchWhenStarted {
            splashViewModel.splashViewStateFlow.collect {
                when (it) {
                    is SplashViewState.Idle -> {
                        // do nothing
                    }
                    is SplashViewState.NavigateToHome -> HomeActivity.start(this@SplashActivity)
                }
            }
        }
    }
}