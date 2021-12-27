package com.android.tvflix.config

import com.android.tvflix.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class AppConfig
@Inject
constructor() {
    companion object {
        const val FETCH_TIMEOUT_S = 20L
        const val MIN_FETCH_INTERVAL_S = 3600L
    }

    private val remoteConfig by lazy { Firebase.remoteConfig }

    fun initialise() {
        val minFetchInterval: Long = if (BuildConfig.DEBUG) {
            0
        } else {
            MIN_FETCH_INTERVAL_S
        }
        val remoteConfigSettings = remoteConfigSettings {
            fetchTimeoutInSeconds = FETCH_TIMEOUT_S
            minimumFetchIntervalInSeconds = minFetchInterval
        }
        remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings)
            setDefaultsAsync(ConfigDefaults.getDefaultValues())
        }
    }

    suspend fun fetch(): Boolean =
        suspendCancellableCoroutine { continuation ->
            remoteConfig.fetchAndActivate().addOnSuccessListener {
                continuation.resume(true)
            }.addOnFailureListener { exc -> continuation.resumeWithException(exc) }
        }

    fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    fun getBoolean(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }

    fun getDouble(key: String): Double {
        return remoteConfig.getDouble(key)
    }

    fun getLong(key: String): Long {
        return remoteConfig.getLong(key)
    }

    fun getInt(key: String): Int {
        return remoteConfig.getLong(key).toInt()
    }
}