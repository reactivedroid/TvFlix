package com.android.ashwiask.tvmaze.home;

import dagger.Subcomponent;

/**
 * @author Ashwini Kumar.
 */
@Subcomponent(modules = HomeModule.class)
public interface HomeComponent
{
    void inject(HomeActivity homeActivity);
}
