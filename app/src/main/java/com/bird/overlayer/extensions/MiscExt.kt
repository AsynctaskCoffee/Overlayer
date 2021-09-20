package com.bird.overlayer.extensions

import android.util.Patterns.WEB_URL
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import java.math.BigInteger
import java.security.MessageDigest

/** Convenience for callbacks/listeners whose return value indicates an event was consumed. */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

fun Int.factor(v: Int) = (this * (v / 100f)).toInt()

fun Char.isPlaceHolder(): Boolean = this == '#'

fun TranslateAnimation.onAnimationEnd(listener: () -> Unit) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationStart(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            listener()
        }
    })
}

fun String.isValidUrl() = WEB_URL.matcher(this).matches()

fun String.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    val convertedString = BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    val stringBuilder = StringBuilder(convertedString)
    stringBuilder.insert(8, "-")
    stringBuilder.insert(13, "-")
    stringBuilder.insert(18, "-")
    stringBuilder.insert(23, "-")

    return stringBuilder.toString()
}

fun Window.keepScreenOn() = addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)