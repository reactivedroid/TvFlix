package com.android.tvflix.config

object ConfigDefaults {
    fun getDefaultValues(): Map<String, Any> {
        return hashMapOf(ConfigKeys.FEATURE_FAVORITES_ENABLE to false)
    }

    object ConfigKeys {
        const val FEATURE_FAVORITES_ENABLE = "feature_favorites_enable"
    }
}