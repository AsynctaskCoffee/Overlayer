package com.bird.overlayer.extensions

import android.graphics.Bitmap
import kotlinx.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun File.saveBitmap(bitmap: Bitmap) {
    this.createNewFile()
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
    //write the bytes in file
    val fos = FileOutputStream(this)
    fos.write(bos.toByteArray())
    fos.flush()
    fos.close()
}