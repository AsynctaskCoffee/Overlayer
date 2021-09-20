package com.bird.overlayer.data.remote.services.providers

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseApiServiceProvider<T>(private val gson: Gson, private val okHttpClient: OkHttpClient)
    : ApiServiceProvider<T> {

    private var _service: T? = null

    override fun getService() = _service

    override fun setUrl(url: String) {
        _service = buildService(url)
    }

    protected abstract fun buildService(url: String) : T

    protected fun buildRetrofit(url: String) = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
}