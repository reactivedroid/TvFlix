package com.android.tvflix.config

import com.android.tvflix.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
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

    private val remoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    fun initialise() {
        val minFetchInterval: Long = if (BuildConfig.DEBUG) {
            0
        } else {
            MIN_FETCH_INTERVAL_S
        }
        val remoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
            .setFetchTimeoutInSeconds(FETCH_TIMEOUT_S)
            .setMinimumFetchIntervalInSeconds(minFetchInterval)
            .build()
        remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings)
            setDefaultsAsync(ConfigDefaults.getDefaultValues())
        }
    }

    suspend fun fetch(): Boolean =
        suspendCancellableCoroutine { continuation ->
            remoteConfig.fetchAndActivate().addOnSuccessListener {
                Timber.i("Config fetched successfully")
                continuation.resume(true)
            }.addOnFailureListener { exc ->
                Timber.i("Config fetch failed")
                continuation.resumeWithException(exc) }
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
