package com.bird.overlayer.di.components

import com.bird.overlayer.di.modules.ApplicationModule
import com.bird.overlayer.di.modules.DbModule
import com.bird.overlayer.di.modules.NetworkModule
import com.bird.overlayer.di.scope.ApplicationScope
import com.bird.overlayer.ui.components.main.MainActivity
import com.bird.overlayer.ui.components.splash.SplashActivity
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class, DbModule::class, NetworkModule::class])
interface ApplicationComponent {

    fun inject(activity: SplashActivity)
    fun inject(activity: MainActivity)
}