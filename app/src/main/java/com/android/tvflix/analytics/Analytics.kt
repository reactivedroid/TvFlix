package com.android.tvflix.analytics

interface Analytics {
    fun sendEvent(event: Event)
    fun setUserId(userId: String)
}