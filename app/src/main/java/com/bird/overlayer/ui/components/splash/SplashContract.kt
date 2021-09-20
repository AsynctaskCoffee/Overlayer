package com.bird.overlayer.ui.components.splash

import com.bird.overlayer.ui.base.BaseContract

interface SplashContract {

    interface View : BaseContract.View {
        fun initUI()
    }

    interface Presenter : BaseContract.Presenter<View>
}