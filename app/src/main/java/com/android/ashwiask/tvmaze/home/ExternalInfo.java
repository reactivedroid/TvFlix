package com.android.ashwiask.tvmaze.home;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author Ashwini Kumar.
 */
@AutoValue
public abstract class ExternalInfo {
    public static TypeAdapter<ExternalInfo> typeAdapter(Gson gson) {
        return new AutoValue_ExternalInfo.GsonTypeAdapter(gson);
    }

    @Nullable
    public abstract String tvrage();

    public abstract long thetvdb();

    @Nullable
    public abstract String imdb();
}
