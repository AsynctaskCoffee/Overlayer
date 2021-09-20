package com.bird.overlayer.data.remote.services

import com.bird.overlayer.data.local.models.Item
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Call
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url


interface RemoteApiService {
    @GET("candidates/overlay.json")
    fun getOverlayList(): Observable<List<Item>>

    @GET
    fun getImageData(@Url url: String): Observable<ResponseBody>
}