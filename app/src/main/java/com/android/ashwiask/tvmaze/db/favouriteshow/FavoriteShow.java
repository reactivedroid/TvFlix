package com.android.ashwiask.tvmaze.db.favouriteshow;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity(tableName = "favourite_shows")
public class FavoriteShow {
    @PrimaryKey
    private long id;

    @Nullable
    private String name;

    @Nullable
    private String premiered;

    @Nullable
    private String imageUrl;

    @Nullable
    private String summary;

    @Nullable
    private String rating;

    private int runtime;

    private boolean favorite;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getPremiered() {
        return premiered;
    }

    public void setPremiered(@Nullable String premiered) {
        this.premiered = premiered;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@Nullable String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Nullable
    public String getSummary() {
        return summary;
    }

    public void setSummary(@Nullable String summary) {
        this.summary = summary;
    }

    @Nullable
    public String getRating() {
        return rating;
    }

    public void setRating(@Nullable String rating) {
        this.rating = rating;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FavoriteShow && ((FavoriteShow) obj).getId() == getId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
