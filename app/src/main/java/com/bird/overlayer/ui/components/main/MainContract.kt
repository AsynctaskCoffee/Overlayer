package com.bird.overlayer.ui.components.main

import android.graphics.Bitmap
import com.bird.overlayer.data.local.models.Item
import com.bird.overlayer.ui.base.BaseContract

interface MainContract {

    interface View : BaseContract.View {

        fun initUI()

        fun showItems(items: List<Item>)

        fun showToast(title: String)

        fun showProgress()

        fun hideProgress()

        fun showProgressWithTitle(title: String)

        fun onMainBitmapReady(result: Bitmap)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun onItemClicked(item: Item)
    }
}