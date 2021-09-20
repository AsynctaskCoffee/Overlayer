package com.bird.overlayer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun formatDateTime(dateString: String, sourceFormat: String, outputFormat: String): String {
        val date = getDateFromString(dateString, sourceFormat)
        return getDateAsString(date, outputFormat)
    }

    fun getDateFromString(dateString: String, sourceFormat: String): Date {
        return SimpleDateFormat(sourceFormat, Locale.US).parse(dateString)
    }

    fun getDateAsString(date: Date, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.US)
        return sdf.format(date)
    }

    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun getMinutesFromSeconds(seconds: Int): Int {
        return (seconds % 3600) / 60
    }
}