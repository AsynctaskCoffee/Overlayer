package com.bird.overlayer.di.modules

import android.app.Application
import com.bird.overlayer.BuildConfig
import com.bird.overlayer.data.remote.interceptors.HeaderInterceptor
import com.bird.overlayer.data.remote.services.RemoteApiService
import com.bird.overlayer.di.scope.ApplicationScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named


@Module
class NetworkModule(private val application: Application) {

    companion object {
        const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MiB
    }

    @ApplicationScope
    @Provides
    fun provideGson() = GsonBuilder().create()

    @ApplicationScope
    @Provides
    fun provideOkHttpCache() = Cache(application.cacheDir, CACHE_SIZE)

    @ApplicationScope
    @Provides
    fun provideHeadersInterceptor() = HeaderInterceptor()

    @ApplicationScope
    @Provides
    @Named("CommonOkHttpClient")
    fun provideOkHttpClient(cache: Cache, headerInterceptor: HeaderInterceptor) =
        with(OkHttpClient.Builder()) {
            cache(cache)
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(logging)
            }
            addInterceptor(headerInterceptor)
            build()
        }

    @ApplicationScope
    @Provides
    @Named("RemoteOkHttpClient")
    fun provideRemoteOkHttpClient(cache: Cache, headerInterceptor: HeaderInterceptor) =
        with(OkHttpClient.Builder()) {
            cache(cache)
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(logging)
            }
            addInterceptor(headerInterceptor)
            build()
        }

    @ApplicationScope
    @Provides
    @Named("Retrofit")
    fun provideRetrofit(gson: Gson, @Named("RemoteOkHttpClient") okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BuildConfig.apiEndpointUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @ApplicationScope
    @Provides
    fun provideRemoteApiService(@Named("Retrofit") retrofit: Retrofit) = retrofit.create(
        RemoteApiService::class.java
    )
}