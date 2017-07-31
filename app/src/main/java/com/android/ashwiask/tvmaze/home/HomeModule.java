package com.android.ashwiask.tvmaze.home;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;

/**
 * @author Ashwini Kumar.
 */
@Module
public class HomeModule
{
    private HomeActivity homeActivity;

    HomeModule(HomeActivity homeActivity)
    {
        this.homeActivity = homeActivity;
    }

    @Provides
    HomeEndpoint provideHomeEndpoint(Retrofit retrofit)
    {
        return retrofit.create(HomeEndpoint.class);
    }

    @Provides
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    HomePresenter provideHomePresenter(HomeEndpoint homeEndpoint, CompositeDisposable compositeDisposable)
    {
        return new HomePresenterImpl(homeActivity, homeEndpoint, compositeDisposable);
    }
}
