package com.bird.overlayer.extensions

import android.app.NotificationManager
import android.content.Context
import android.os.PowerManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat


fun Context.color(@ColorRes colorResource: Int): Int {
    return ContextCompat.getColor(this, colorResource)
}

fun Context.inflate(@LayoutRes layoutRes: Int): View? {
    return LayoutInflater.from(this).inflate(layoutRes, null)
}

inline val Context.notificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

inline val Context.inputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

inline val Context.powerManager
    get() = getSystemService(Context.POWER_SERVICE) as PowerManager?

inline val Context.windowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

fun Context.getPxFromDp(dps: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, resources.displayMetrics)
        .toInt()
}


