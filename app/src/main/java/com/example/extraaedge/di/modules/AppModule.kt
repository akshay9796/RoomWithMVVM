package com.example.extraaedge.di.modules

import android.content.Context
import com.example.extraaedge.App
import dagger.Module
import dagger.Provides

/**
 * This is where you will inject application-wide dependencies.
 */
@Module
class AppModule {
    @Provides
    fun provideContext(application: App): Context {
        return application.getApplicationContext()
    }
}