package com.android.ashwiask.tvmaze.home;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ashwini Kumar.
 */
@AutoValue
public abstract class Show implements Parcelable {

    public static Show fromFavoriteShow(FavoriteShow favoriteShow) {
        Map<String, String> image = new HashMap<>(1);
        image.put("original", favoriteShow.getImageUrl());
        Map<String, String> rating = new HashMap<>(1);
        image.put("average", favoriteShow.getRating());
        return Show.builder()
                .id(favoriteShow.getId())
                .name(favoriteShow.getName())
                .summary(favoriteShow.getSummary())
                .image(image)
                .isFavorite(true)
                .premiered(favoriteShow.getPremiered())
                .rating(rating)
                .runtime(favoriteShow.getRuntime())
                .build();
    }

    public static TypeAdapter<Show> typeAdapter(Gson gson) {
        return new AutoValue_Show.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_Show.Builder();
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

    @Nullable
    public abstract String premiered();

    @Nullable
    public abstract String officialSite();

    @Nullable
    @SerializedName("network")
    public abstract Channel airChannel();

    @Nullable
    @SerializedName("webChannel")
    public abstract Channel webChannel();

    @Nullable
    public abstract Map<String, String> image();

    @Nullable
    @SerializedName("externals")
    public abstract ExternalInfo externalInfo();

    @Nullable
    public abstract String summary();

    @Nullable
    public abstract Map<String, String> rating();

    public abstract boolean isFavorite();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder id(long id);

        public abstract Builder url(String url);

        public abstract Builder premiered(String premiered);

        public abstract Builder type(String type);

        public abstract Builder status(String status);

        public abstract Builder language(String language);

        public abstract Builder genres(List<String> genres);

        public abstract Builder runtime(int runtime);

        public abstract Builder officialSite(String officialSite);

        public abstract Builder airChannel(Channel airChannel);

        public abstract Builder webChannel(Channel webChannel);

        public abstract Builder externalInfo(ExternalInfo externalInfo);

        public abstract Builder isFavorite(boolean isFavorite);

        public abstract Builder name(String name);

        public abstract Builder rating(Map<String, String> rating);

        public abstract Builder summary(String summary);

        public abstract Builder image(Map<String, String> image);

        public abstract Show build();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Show && id() == ((Show) obj).id();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
