package com.bird.overlayer.data

import com.bird.overlayer.data.local.LocalDataSource
import com.bird.overlayer.data.local.models.Item
import com.bird.overlayer.data.remote.RemoteDataSource
import com.bird.overlayer.utils.PrefManager
import javax.inject.Inject

open class DataRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val prefManager: PrefManager
) {
    fun getCount(): Int = localDataSource.getCount()
    fun getItemsFromLocal() = localDataSource.getItems()
    fun storeItems(items: List<Item>) = localDataSource.storeItems(items)
    fun setDownloadedImage(items: List<Item>) = localDataSource.setDownloadedImage(items)
    fun getItemsFromRemote() = remoteDataSource.getOverlayList()
    fun getImageData(url: String) = remoteDataSource.getImageData(url)
}