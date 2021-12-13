package com.android.tvflix.analytics

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsHandler
@Inject
constructor() : Analytics {
    private val firebaseAnalytics by lazy { Firebase.analytics }
    override fun sendEvent(event: Event) {
        firebaseAnalytics.logEvent(event.eventName) {
            param(EventParams.CONTENT_ID, event.eventParams[EventParams.CONTENT_ID] as Long)
            param(EventParams.CONTENT_TITLE, event.eventParams[EventParams.CONTENT_TITLE] as String)
            param(
                EventParams.IS_FAVORITE_MARKED,
                event.eventParams[EventParams.IS_FAVORITE_MARKED].toString()
            )
            param(
                EventParams.CLICK_CONTEXT,
                event.eventParams[EventParams.CLICK_CONTEXT] as String
            )
        }
    }

    override fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
    }
}