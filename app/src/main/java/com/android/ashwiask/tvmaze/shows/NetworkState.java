package com.android.ashwiask.tvmaze.shows;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@AutoValue
public abstract class NetworkState {
    public static Builder builder() {
        return new AutoValue_NetworkState.Builder()
                .status(Status.LOADING);
    }

    public abstract int status();

    @Nullable
    public abstract String message();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder status(int status);

        public abstract Builder message(String message);

        public abstract NetworkState build();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Status.LOADING, Status.SUCCESS, Status.ERROR})
    public @interface Status {
        int LOADING = 0;
        int SUCCESS = 1;
        int ERROR = 2;
    }
}
