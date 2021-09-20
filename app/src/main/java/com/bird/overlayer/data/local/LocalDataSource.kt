package com.bird.overlayer.data.local

import com.bird.overlayer.data.local.models.Item
import javax.inject.Inject

open class LocalDataSource @Inject constructor(private val appDatabaseWrapper: AppDatabaseWrapper) {
    private val appDatabase: AppDatabase get() = appDatabaseWrapper.getAppDatabase()
    fun getCount(): Int = appDatabase.itemDao().getCount()
    fun storeItems(items: List<Item>) = appDatabase.itemDao().insert(items)
    fun setDownloadedImage(items: List<Item>) = appDatabase.itemDao().setDownloadedImage(items)
    fun getItems() = appDatabase.itemDao().getItems()
}