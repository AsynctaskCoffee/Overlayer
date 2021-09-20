package com.bird.overlayer.ui.base

import androidx.lifecycle.ViewModel

class BaseViewModel<V : BaseContract.View, P : BaseContract.Presenter<V>> : ViewModel() {

    var presenter: P? = null

    override fun onCleared() {
        super.onCleared()
        presenter?.onPresenterDestroyed()
        presenter = null
    }
}