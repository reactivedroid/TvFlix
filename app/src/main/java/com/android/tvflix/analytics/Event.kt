package com.android.tvflix.analytics

data class Event(val eventName: String, val eventParams: Map<String, Any>)

object EventParams {
    const val CONTENT_ID = "content_id"
    const val CONTENT_TITLE = "content_title"
    const val IS_FAVORITE_MARKED = "is_favorite_marked"
    const val CLICK_CONTEXT = "CLICK_CONTEXT"
}

object EventNames {
    const val CLICK = "click"
}
