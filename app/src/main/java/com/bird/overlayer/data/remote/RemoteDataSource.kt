package com.bird.overlayer.data.remote

import com.bird.overlayer.data.remote.interceptors.HeaderInterceptor
import com.bird.overlayer.data.remote.services.RemoteApiService
import javax.inject.Inject

open class RemoteDataSource @Inject constructor(
    private val headerInterceptor: HeaderInterceptor,
    private val remoteApiService: RemoteApiService
) {
    fun addTokenInHeaderInterceptor(registrationToken: String) {
        headerInterceptor.setRegistrationToken(registrationToken)
    }
    fun getOverlayList() = remoteApiService.getOverlayList()
    fun getImageData(url: String) = remoteApiService.getImageData(url)
}