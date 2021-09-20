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

    private fun getItemsFromServer() {
        disposables?.addAll(
            dataRepository.getItemsFromRemote()
                .applySchedulers()
                .doOnSubscribe {
                    getView()?.showProgressWithTitle("Connecting to remote server...")
                }
                .subscribe({
                    if (it.isNotEmpty()) {
                        saveItemsToLocal(it)
                    }
                }, {
                    print("Error getItemsFromServer: ${it.message}")
                    getView()?.showToast("Error getItemsFromServer: ${it.message}")
                })
        )
    }

    private fun getLocalCount() {
        disposables?.addAll(
            Observable.fromCallable {
                dataRepository.getCount()
            }
                .applySchedulers()
                .doOnSubscribe {
                    getView()?.showProgress()
                }
                .subscribe({

                }, {
                    print("Error getLocalCount: ${it.message}")
                    getView()?.showToast("Error getLocalCount: ${it.message}")
                })
        )

    }

    private fun getImageData(items: List<Item>, reverse: Boolean) {
        disposables?.addAll(
            Observable.fromIterable(items)
                .doOnSubscribe {
                    getView()?.showProgressWithTitle("Downloading assets...")
                }
                .flatMap {
                    dataRepository.getImageData(if (reverse) it.overlayUrl else it.overlayPreviewIconUrl)
                }
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .toList()
                .doOnSuccess {
                    if (reverse)
                        getImageData(items, false)
                    else
                        saveImagesToLocal(items)
                }
                .subscribe { itemsList ->
                    for (i in itemsList.indices) {
                        if (reverse)
                            items[i].data = itemsList[i].bytes()
                        else items[i].dataThumb = itemsList[i].bytes()
                    }
                }
        )

    }


    private fun saveItemsToLocal(items: List<Item>) {
        disposables?.add(
            Observable.fromCallable {
                dataRepository.storeItems(items)
            }.subscribeOn(Schedulers.newThread())
                .doOnSubscribe {
                    getView()?.showProgressWithTitle("Saving local data...")
                }
                .subscribe({
                    getItemsFromLocal()
                }, {
                    print("Error saveItemsToLocal: ${it.message}")
                    getView()?.showToast("Error saveItemsToLocal: ${it.message}")
                })
        )
    }

    private fun saveImagesToLocal(items: List<Item>) {
        disposables?.add(
            Observable.fromCallable {
                dataRepository.setDownloadedImage(items)
            }.subscribeOn(Schedulers.newThread())
                .doOnSubscribe {
                    getView()?.showProgressWithTitle("Saving assets...")
                }
                .subscribe({
                    getItemsFromLocal()
                }, {
                    print("Error saveItemsToLocal: ${it.message}")
                    getView()?.showToast("Error saveItemsToLocal: ${it.message}")
                })
        )
    }

    private fun isImageDownloadNeeded(items: List<Item>): Boolean {
        var isNeeded = false
        for (i in items)
            if (i.data == null || i.dataThumb == null) {
                isNeeded = true
                break
            }
        return isNeeded
    }

    private fun getItemsFromLocal() {
        disposables?.add(
            dataRepository.getItemsFromLocal()
                .doOnSubscribe {
                    getView()?.showProgressWithTitle("Checking local database")
                }
                .applySchedulers()
                .subscribe({
                    when {
                        it.isEmpty() -> {
                            getItemsFromServer()
                        }
                        isImageDownloadNeeded(it) -> {
                            getImageData(it, true)
                        }
                        else -> {
                            getView()?.showItems(it)
                            getView()?.hideProgress()
                        }
                    }
                }, {
                    getView()?.hideProgress()
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