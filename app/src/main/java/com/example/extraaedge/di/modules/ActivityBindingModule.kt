package com.example.extraaedge.di.modules

import com.example.extraaedge.ui.rocket.RocketListActivity
import com.example.extraaedge.ui.rocket_details.RocketDeatilsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun bindRocketListActivity(): RocketListActivity?

    @ContributesAndroidInjector
    abstract fun bindRocketDeatilsActivity(): RocketDeatilsActivity?
}