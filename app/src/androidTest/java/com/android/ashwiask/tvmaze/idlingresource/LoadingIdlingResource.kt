package com.android.ashwiask.tvmaze.idlingresource

import android.app.Activity
import android.view.View
import android.widget.ProgressBar
import androidx.test.espresso.IdlingResource
import com.android.ashwiask.tvmaze.R

/**
 * An idling resource which checks if the loader is being shown on screen
 */
class LoadingIdlingResource constructor(private val activity: Activity) : IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null
    override fun getName(): String {
        return "LoadingIdlingResource"
    }

    override fun isIdleNow(): Boolean {
        return if (!isProgressShowing()) {
            resourceCallback?.onTransitionToIdle()
            true
        } else {
            false
        }
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }

    private fun isProgressShowing(): Boolean {
        val progress = activity.findViewById<ProgressBar>(R.id.progress)
        return progress.visibility == View.VISIBLE
    }
}