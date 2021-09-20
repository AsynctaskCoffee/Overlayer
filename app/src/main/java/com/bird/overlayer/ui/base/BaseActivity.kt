package com.bird.overlayer.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bird.overlayer.App

abstract class BaseActivity<V : BaseContract.View, P : BaseContract.Presenter<V>> :
    AppCompatActivity(), BaseContract.View {

    protected var presenter: P? = null
    protected var hasSavedInstance: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasSavedInstance = savedInstanceState != null
        setContentView(getLayoutResId())

        injectDependencies()

        val viewModel: BaseViewModel<V, P> =
            ViewModelProviders.of(this).get(BaseViewModel<V, P>()::class.java)
        val isCreated = viewModel.presenter == null
        viewModel.presenter = viewModel.presenter ?: initPresenter()
        presenter = viewModel.presenter
        presenter?.attachView(this as V)
        presenter?.onSavedInstance(hasSavedInstance)
        if (isCreated) presenter?.onPresenterCreated()
        presenter?.onCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    override fun onStart() {
        super.onStart()
        presenter?.onStarted()
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResumed()
    }

    override fun onPause() {
        super.onPause()
        presenter?.onPaused()
    }

    override fun handleLongPress(): Boolean {
        return false
    }

    fun getApplicationComponent() = (application as App).applicationComponent

    protected abstract fun initPresenter(): P

    protected abstract fun injectDependencies()

    protected abstract fun getLayoutResId(): Int
}