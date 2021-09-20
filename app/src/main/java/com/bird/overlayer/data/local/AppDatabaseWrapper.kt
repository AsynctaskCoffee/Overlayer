package com.bird.overlayer.data.local

import android.content.Context
import androidx.room.Room
import com.bird.overlayer.utils.Constants.Companion.DATABASE_NAME
import com.bird.overlayer.utils.PrefManager

class AppDatabaseWrapper(private val context: Context, private val prefManager: PrefManager) {

    private var appDatabaseInstance: AppDatabase? = null

    @Synchronized
    fun getAppDatabase(): AppDatabase {

        if (appDatabaseInstance == null) {
            val builder = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            appDatabaseInstance = builder.fallbackToDestructiveMigration().build()
        }
        return appDatabaseInstance!!

    }
}