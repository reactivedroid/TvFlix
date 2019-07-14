package com.android.ashwiask.tvmaze.network;

import android.content.Context;

import com.android.ashwiask.tvmaze.network.backend.TvMazeBackendModule;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author Ashwini Kumar.
 */
@Module(includes = TvMazeBackendModule.class)
public class NetworkModule {
    public static final String TVMAZE_BASE_URL = "tvmaze_base_url";
    private static final String BASE_URL = "https://api.tvmaze.com/";

    @Provides
    @Named(TVMAZE_BASE_URL)
    String provideBaseUrlString() {
        return BASE_URL;
    }

    @Provides
    @Singleton
    CookieManager provideCookieManager() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        return cookieManager;
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson(TypeAdapterFactory tvMazeTypeAdaptorFactory) {
        return new GsonBuilder()
                .registerTypeAdapterFactory(tvMazeTypeAdaptorFactory)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    Cache provideCache(Context context) {
        final int cacheSize = 5 * 1024 * 1024; // 5 MB
        File cacheDir = context.getCacheDir();
        return new Cache(cacheDir, cacheSize);
    }

    @Provides
    @Singleton
    RxJava2CallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    @Provides
    @Singleton
    TypeAdapterFactory provideTypeAdapterFactory() {
        return TvMazeTypeAdaptorFactory.create();
    }
}
