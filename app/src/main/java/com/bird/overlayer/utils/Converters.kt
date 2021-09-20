package com.bird.overlayer.utils

import androidx.room.TypeConverter
import com.bird.overlayer.data.remote.models.Param
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class Converters {

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromArray(array: Array<Int>): String {
        return Gson().toJson(array)
    }

    @TypeConverter
    fun fromString(value: String): Array<Int> {
        val listType = object : TypeToken<Array<Int>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Param>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromParam(value: String): List<Param> {
        val listType = object : TypeToken<List<Param>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromInt(value: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {
        }.type
        return Gson().fromJson(value, listType)
    }
}