package com.android.ashwiask.tvmaze.favorite;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.ashwiask.tvmaze.R;
import com.android.ashwiask.tvmaze.base.TvMazeBaseActivity;
import com.android.ashwiask.tvmaze.common.Constants;
import com.android.ashwiask.tvmaze.common.GridItemDecoration;
import com.android.ashwiask.tvmaze.databinding.ActivityFavoriteShowsBinding;
import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FavoriteShowsActivity extends TvMazeBaseActivity
        implements FavoriteShowsAdapter.Callback {
    private static final int FAVORITE_ICON_START_OFFSET = 13;
    private static final int FAVORITE_ICON_END_OFFSET = 14;
    public static final String EXTRAS_REMOVED_FROM_FAVORITE_SHOWS =
            "extras_removed_from_favorite_shows";
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private FavoriteShowsViewModel favoriteShowsViewModel;
    private ActivityFavoriteShowsBinding binding;

    public static void startForResult(Activity context, int requestCode) {
        Intent starter = new Intent(context, FavoriteShowsActivity.class);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_shows);
        setToolbar();
        favoriteShowsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(FavoriteShowsViewModel.class);
        favoriteShowsViewModel.loadFavoriteShows();
        favoriteShowsViewModel.getFavoriteShowsLiveData().observe(this, this::showFavorites);
    }

    private void setToolbar() {
        Toolbar toolbar = binding.toolbar.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.favorite_shows);
    }


    private void showFavorites(List<FavoriteShow> favoriteShows) {
        binding.progress.setVisibility(View.GONE);
        if (!favoriteShows.isEmpty()) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, Constants.NO_OF_COLUMNS);
            binding.shows.setLayoutManager(layoutManager);
            FavoriteShowsAdapter favoriteShowsAdapter = new FavoriteShowsAdapter(favoriteShows, this);
            binding.shows.setAdapter(favoriteShowsAdapter);
            int spacing = getResources().getDimensionPixelSize(R.dimen.show_grid_spacing);
            binding.shows.addItemDecoration(new GridItemDecoration(spacing, Constants.NO_OF_COLUMNS));
            binding.shows.setVisibility(View.VISIBLE);
        } else {
            ImageSpan bookmarkSpan = new ImageSpan(this, R.drawable.favorite_border);
            SpannableString spannableString = new SpannableString(getString(R.string.favorite_hint_msg));
            spannableString.setSpan(bookmarkSpan, FAVORITE_ICON_START_OFFSET,
                    FAVORITE_ICON_END_OFFSET, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.favoriteHint.setText(spannableString);
            binding.favoriteHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFavoriteClicked(FavoriteShow show) {
        if (show.isFavorite()) {
            favoriteShowsViewModel.addToFavorite(show);
            Toast.makeText(this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
        } else {
            favoriteShowsViewModel.removeFromFavorite(show);
            Toast.makeText(this, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(EXTRAS_REMOVED_FROM_FAVORITE_SHOWS,
                (ArrayList<? extends Parcelable>) favoriteShowsViewModel.getRemovedFromFavoriteShows());
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}
