package com.android.ashwiask.tvmaze.network.backend;

import com.android.ashwiask.tvmaze.home.TvMazeApi;
import com.android.ashwiask.tvmaze.network.NetworkModule;
import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class TvMazeBackendModule {

    @Provides
    @Singleton
    public TvMazeApi provideTvMazeApi(OkHttpClient okHttpClient,
                                      @Named(NetworkModule.TVMAZE_BASE_URL) String baseUrl,
                                      RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                                      Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(okHttpClient)
                .build().create(TvMazeApi.class);
    }
}
