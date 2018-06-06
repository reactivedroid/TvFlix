package com.android.ashwiask.tvmaze.home;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author Ashwini Kumar.
 */

@AutoValue
public abstract class AirChannel {
    public static TypeAdapter<AirChannel> typeAdapter(Gson gson) {
        return new AutoValue_AirChannel.GsonTypeAdapter(gson);
    }
    public abstract long id();

    public abstract String name();
}
