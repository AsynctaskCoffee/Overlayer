package com.bird.overlayer.ui.base

interface BaseContract {

    interface View {
        fun handleLongPress(): Boolean?
     }

    interface Presenter<V : View> {

        fun getView(): V?

        fun attachView(view: V)

        fun detachView()

        fun onPresenterCreated()

        fun onPresenterDestroyed()

        fun onCreated()

        fun onStarted()

        fun onResumed()

        fun onDestroyed()

        fun onStopped()

        fun onPaused()

        fun onUserInteraction()

        fun onLongPressed(): Boolean?

        fun onBackPressed(): Boolean

        fun onSavedInstance(hasSavedInstance: Boolean)

        fun onKeyboardOpened(opened: Boolean)

        fun isKeyboardOpened():Boolean
    }
}