package com.bird.overlayer.ui.components.splash

import com.bird.overlayer.ui.base.BasePresenter
import javax.inject.Inject

class SplashPresenter @Inject constructor() : BasePresenter<SplashContract.View>(),
    SplashContract.Presenter {
    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
    }
}