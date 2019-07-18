package com.android.ashwiask.tvmaze

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.android.ashwiask.tvmaze.di.AppComponent
import com.android.ashwiask.tvmaze.di.AppInjector
import com.android.ashwiask.tvmaze.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber

import javax.inject.Inject

/**
 * @author Ashwini Kumar.
 */
class TvMazeApplication : MultiDexApplication(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant()
        }
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
        appComponent.inject(this)
        AppInjector.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }
}
