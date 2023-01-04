package com.example.extraaedge.di.modules

import com.example.extraaedge.BuildConfig
import com.example.extraaedge.network.ApiInterceptor
import com.example.extraaedge.network.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    var dispatcher: Dispatcher? = null

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideApiService(gson: Gson?, apiInterceptor: ApiInterceptor): ApiService {
        val interceptors: MutableList<Interceptor> =
            ArrayList()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        if (BuildConfig.DEBUG) {
            interceptors.add(loggingInterceptor)
        }
        interceptors.add(apiInterceptor)
        val builder = buildOkHttpClientBuilder(interceptors)
        dispatcher = Dispatcher()
        dispatcher?.maxRequests = 5
        builder.dispatcher(dispatcher!!)
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(builder.build())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun buildOkHttpClientBuilder(interceptors: List<Interceptor>?): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        if (interceptors != null) {
            for (interceptor in interceptors) builder.addInterceptor(
                interceptor
            )
        }
        builder.connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
        return builder
    }

    companion object {
        private const val BASE_URL = "https://api.spacexdata.com/v4/"
        const val IMAGE_BASE_URL = "/"
    }
}