package com.bird.overlayer.di.modules

import android.app.Application
import com.bird.overlayer.data.local.AppDatabaseWrapper
import com.bird.overlayer.di.scope.ApplicationScope
import com.bird.overlayer.utils.PrefManager
import dagger.Module
import dagger.Provides

@Module
class DbModule(private val application: Application) {

    @ApplicationScope
    @Provides
    fun providesAppDatabaseWrapper(prefManager: PrefManager) =
        AppDatabaseWrapper(application.applicationContext, prefManager)
}
