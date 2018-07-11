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

    public static Builder builder() {
        return new AutoValue_Episode.Builder();
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

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder show(Show show);

        public abstract Builder id(long id);

        @Nullable
        public abstract Builder url(String url);

        @Nullable
        public abstract Builder name(String name);

        public abstract Builder season(int season);

        public abstract Builder number(int number);

        @Nullable
        public abstract Builder airdate(String airdate);

        @Nullable
        public abstract Builder airtime(String airtime);

        public abstract Builder runtime(int runtime);

        @Nullable
        public abstract Builder summary(String summary);

        public abstract Episode build();
    }
}
