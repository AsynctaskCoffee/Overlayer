package com.bird.overlayer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bird.overlayer.data.local.models.Item
import io.reactivex.Single

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Item>)

    @Query("SELECT * FROM Item")
    fun getItems(): Single<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setDownloadedImage(image: List<Item>)

    @Query("SELECT COUNT(*) FROM Item")
    fun getCount(): Int
}