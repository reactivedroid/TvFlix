package com.android.ashwiask.tvmaze.home;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author Ashwini Kumar.
 */

@AutoValue
public abstract class Channel implements Parcelable {
    public static TypeAdapter<Channel> typeAdapter(Gson gson) {
        return new AutoValue_Channel.GsonTypeAdapter(gson);
    }

    public abstract long id();

    public abstract String name();

    @Nullable
    public abstract Country country();
}
