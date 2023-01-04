package com.example.extraaedge

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDex
import com.example.extraaedge.di.AppComponent
import com.example.extraaedge.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector, HasSupportFragmentInjector, HasServiceInjector {

    var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null
        @Inject set

    var mFragmentInjector: DispatchingAndroidInjector<Fragment>? = null
        @Inject set

    var mServiceInjector: DispatchingAndroidInjector<Service>? = null
        @Inject set

    private var activitiesCount = 0

    override fun onCreate() {
        super.onCreate()

        val appComponent: AppComponent? = DaggerAppComponent.builder().application(this).build()
        appComponent?.inject(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector!!
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return mFragmentInjector!!
    }


    fun onActivityResume() {
        activitiesCount++ // on become foreground
    }

    fun onActivityPaused() {
        --activitiesCount // on become background
    }

    private val isInForeground: Boolean
        get() = activitiesCount > 0

    override fun serviceInjector(): AndroidInjector<Service> {
        return mServiceInjector!!
    }
}