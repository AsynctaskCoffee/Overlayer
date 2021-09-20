package com.bird.overlayer.data.remote.services.providers

import com.bird.overlayer.data.remote.services.RemoteApiService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import javax.inject.Inject

class DeviceApiServiceProvider @Inject constructor(gson: Gson, okHttpClient: OkHttpClient) :
    BaseApiServiceProvider<RemoteApiService>(gson, okHttpClient) {

    override fun buildService(url: String) = buildRetrofit(url).create(RemoteApiService::class.java)
}