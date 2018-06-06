package com.android.ashwiask.tvmaze.home;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author Ashwini Kumar.
 */
@AutoValue
public abstract class Show {
    public static TypeAdapter<Show> typeAdapter(Gson gson) {
        return new AutoValue_Show.GsonTypeAdapter(gson);
    }

    public abstract long id();

    @Nullable
    public abstract String url();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String type();

    @Nullable
    public abstract String language();

    @Nullable
    public abstract List<String> genres();

    @Nullable
    public abstract String status();

    public abstract int runtime();

    public abstract String premiered();

    @Nullable
    public abstract String officialSite();

    @Nullable
    @SerializedName("network")
    public abstract AirChannel airChannel();

    @Nullable
    public abstract Map<String, String> image();

    @Nullable
    @SerializedName("externals")
    public abstract ExternalInfo externalInfo();

    @Nullable
    public abstract String summary();

    @Nullable
    public abstract Map<String, Double> rating();
}
