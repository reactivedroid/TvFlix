package com.android.tvflix.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsHandler
@Inject
constructor(@ApplicationContext context: Context) : Analytics {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun sendEvent(event: Event) {
        val bundle = Bundle().apply {
            putLong(EventParams.CONTENT_ID, event.eventParams[EventParams.CONTENT_ID] as Long)
            putString(EventParams.CONTENT_TITLE, event.eventParams[EventParams.CONTENT_TITLE] as String)
            putString(
                EventParams.IS_FAVORITE_MARKED,
                event.eventParams[EventParams.IS_FAVORITE_MARKED].toString()
            )
            putString(
                EventParams.CLICK_CONTEXT,
                event.eventParams[EventParams.CLICK_CONTEXT] as String
            )
        }
        firebaseAnalytics.logEvent(event.eventName, bundle)
    }

    override fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
    }
}
