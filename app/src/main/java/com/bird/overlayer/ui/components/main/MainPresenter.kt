package com.bird.overlayer.ui.components.main

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import com.bird.overlayer.data.DataRepository
import com.bird.overlayer.data.local.models.Item
import com.bird.overlayer.extensions.applySchedulers
import com.bird.overlayer.ui.base.BasePresenter
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val dataRepository: DataRepository
) : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        getItemsFromLocal()
    }

    private fun getItemsFromLocal() {
        disposables?.add(
            dataRepository.getItemsFromLocal()
                .applySchedulers()
                .subscribe({
                    getView()?.showItems(it)
                },
                    {
                        print("Error getItemsFromLocal: ${it.message}")
                        getView()?.showToast("Error getItemsFromLocal: ${it.message}")
                    })
        )
    }

    override fun onItemClicked(item: Item) {
        getView()?.showToast(item.overlayName)
    }


    fun captureViewAsBitmap(v: View): Bitmap {
        val b = Bitmap.createBitmap(
            v.measuredWidth,
            v.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }

    // correction for captured images
    fun prepareMainBitmap(uri: Uri?, contentResolver: ContentResolver) {
        if (uri != null) {
            val result: String?
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            if (cursor == null) {
                result = uri.path
            } else {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
                cursor.close()
            }

            val ei = ExifInterface(result)

            val orientation: Int = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val rotatedBitmap: Bitmap?
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    rotatedBitmap = rotateImage(bitmap, 90)
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    rotatedBitmap = rotateImage(bitmap, 180)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    rotatedBitmap = rotateImage(bitmap, 270)
                }
                ExifInterface.ORIENTATION_NORMAL -> {
                    rotatedBitmap = bitmap
                }
                else -> {
                    rotatedBitmap = bitmap
                }
            }
            getView()?.onMainBitmapReady(rotatedBitmap)
        }
    }
}