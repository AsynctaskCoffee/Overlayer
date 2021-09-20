package com.bird.overlayer.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.FileProvider
import com.bird.overlayer.BuildConfig
import java.io.File

/**
 * Extensions for simpler launching of Activities
 */
inline fun <reified T : Any> Activity.launchActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> Context.launchActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)

fun Context.launchActionView(file: File, mimeType: String?): Boolean {
    val fileURI = getFileUri(this, file)

    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(fileURI, mimeType)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    val isIntentSafe = isIntentSafe(this , intent)
    if(isIntentSafe) startActivity(intent)

    return isIntentSafe
}

fun Context.launchActionSend(file: File): Boolean {
    val fileURI = getFileUri(this, file)

    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "email/gmail"
    intent.putExtra(Intent.EXTRA_STREAM, fileURI)
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    val isIntentSafe = isIntentSafe(this , intent)
    if(isIntentSafe) startActivity(intent)

    return isIntentSafe
}

private fun getFileUri(context: Context, file: File) = FileProvider.getUriForFile(context,
        BuildConfig.APPLICATION_ID + ".provider", file)

private fun isIntentSafe(context: Context, intent: Intent) = context.packageManager.queryIntentActivities(intent, 0)
        .isNotEmpty()
