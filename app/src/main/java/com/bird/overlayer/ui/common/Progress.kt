package com.bird.overlayer.ui.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.bird.overlayer.R
import com.bird.overlayer.extensions.inflate

class Progress constructor(
    context: Context?,
    @StringRes private val titleRes: Int,
    cancelable: Boolean = false
) {

    private var view: View? = null
    private var builder: AlertDialog.Builder
    private var dialog: Dialog

    init {
        view = context?.inflate(R.layout.progress_layout)
        view?.findViewById<TextView>(R.id.text)?.setText(titleRes)
        builder = AlertDialog.Builder(context)
        builder.setView(view)
        dialog = builder.create()
        dialog.setCancelable(cancelable)
    }

    fun setProgressMessage(@StringRes titleRes: Int) {
        view?.findViewById<TextView>(R.id.text)?.setText(titleRes)
    }

    fun setProgressMessage(titleRes: String) {
        view?.findViewById<TextView>(R.id.text)?.text = titleRes
    }

    fun show() {
        if (!dialog.isShowing)
            dialog.show()
    }

    fun isShowing() = dialog.isShowing

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}