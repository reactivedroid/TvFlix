package com.android.ashwiask.tvmaze.home;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author Ashwini Kumar.
 */
@AutoValue
public abstract class Episode {
    public static TypeAdapter<Episode> typeAdapter(Gson gson) {
        return new AutoValue_Episode.GsonTypeAdapter(gson);
    }

    public abstract Show show();

    public abstract long id();

    @Nullable
    public abstract String url();

    @Nullable
    public abstract String name();

    public abstract int season();

    public abstract int number();

    @Nullable
    public abstract String airdate();

    @Nullable
    public abstract String airtime();

    public abstract int runtime();

    @Nullable
    public abstract String summary();
}
