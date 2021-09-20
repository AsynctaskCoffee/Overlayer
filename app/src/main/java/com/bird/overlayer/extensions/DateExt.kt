package com.bird.overlayer.extensions

import java.text.SimpleDateFormat
import java.util.*

const val DF_Time = "HH:mm"

fun Date.formatTime() = SimpleDateFormat(DF_Time, Locale.US).format(this)

fun Date.format(format:String): String? = SimpleDateFormat(format, Locale.US).format(this)