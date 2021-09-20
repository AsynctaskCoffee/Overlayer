package com.bird.overlayer.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.bird.overlayer.data.DataRepository
import com.bird.overlayer.data.local.LocalDataSource
import com.bird.overlayer.data.remote.RemoteDataSource
import com.bird.overlayer.di.scope.ApplicationScope
import com.bird.overlayer.models.AppInfo
import com.bird.overlayer.utils.PrefManager
import com.bird.overlayer.utils.RxBus
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module
class ApplicationModule(private val application: Application) {

    @ApplicationScope
    @Provides
    fun providesApplicationContext(): Context = application.applicationContext

    @ApplicationScope
    @Provides
    fun providesSharedpreferences(): SharedPreferences =
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(application)

    @ApplicationScope
    @Provides
    fun providerRxBus(publisher: PublishSubject<Any>) = RxBus(publisher)

    @ApplicationScope
    @Provides
    fun providerDisplayMetrics() = application.resources.displayMetrics

    @ApplicationScope
    @Provides
    fun providerPublishSubject(): PublishSubject<Any> = PublishSubject.create<Any>()

    @ApplicationScope
    @Provides
    fun providesDataRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        prefManager: PrefManager
    ) = DataRepository(
        localDataSource,
        remoteDataSource,
        prefManager
    )

    @ApplicationScope
    @Provides
    fun providesAppInfo() = AppInfo()
}
