package com.bird.overlayer.ui.components.splash

import com.bird.overlayer.ui.base.BaseContract

interface SplashContract {

    interface View : BaseContract.View {
        fun initUI()
        fun showToast(title: String)
        fun showProgress()
        fun hideProgress()
        fun showProgressWithTitle(title: String)
        fun onDataReady()
    }

    interface Presenter : BaseContract.Presenter<View>
}