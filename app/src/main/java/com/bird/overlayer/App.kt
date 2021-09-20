package com.bird.overlayer

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.bird.overlayer.di.components.ApplicationComponent
import com.bird.overlayer.di.components.DaggerApplicationComponent
import com.bird.overlayer.di.modules.ApplicationModule
import com.bird.overlayer.di.modules.DbModule
import com.bird.overlayer.di.modules.NetworkModule

class App : Application(), LifecycleObserver {

    val applicationComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .dbModule(DbModule(this))
            .networkModule(NetworkModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

}