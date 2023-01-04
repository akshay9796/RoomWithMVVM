package com.example.extraaedge.di

import com.example.extraaedge.App
import com.example.extraaedge.di.modules.ActivityBindingModule
import com.example.extraaedge.di.modules.AppModule
import com.example.extraaedge.di.modules.NetworkModule
import com.example.extraaedge.room.AppDatabase
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ /* Use AndroidInjectionModule.class if you're not using support library */
    AndroidSupportInjectionModule::class, AppModule::class, NetworkModule::class, AppDatabase::class, ActivityBindingModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder
        fun build(): AppComponent?
    }

    fun inject(app: App)
}