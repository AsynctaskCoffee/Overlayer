package com.bird.overlayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bird.overlayer.data.local.dao.ItemDao
import com.bird.overlayer.data.local.models.Item
import com.bird.overlayer.utils.Converters

@Database(entities = [Item::class], version = 3, exportSchema = false)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}