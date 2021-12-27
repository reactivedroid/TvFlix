package com.android.tvflix

import android.app.Application
import com.android.tvflix.config.AppConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TvFlixApplication : Application() {
    @Inject
    lateinit var appConfig: AppConfig

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        appConfig.initialise()
    }
}
