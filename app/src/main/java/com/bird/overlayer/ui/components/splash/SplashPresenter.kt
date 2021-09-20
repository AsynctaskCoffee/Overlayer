package com.bird.overlayer.ui.components.splash

import com.bird.overlayer.data.DataRepository
import com.bird.overlayer.data.local.models.Item
import com.bird.overlayer.extensions.applySchedulers
import com.bird.overlayer.ui.base.BasePresenter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashPresenter @Inject constructor(
    private val dataRepository: DataRepository
) : BasePresenter<SplashContract.View>(),
    SplashContract.Presenter {

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
                    getView()?.showProgressWithTitle("Checking local database...")
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
                            getView()?.onDataReady()
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
}