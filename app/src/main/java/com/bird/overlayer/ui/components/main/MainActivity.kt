package com.bird.overlayer.ui.components.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.bird.overlayer.R
import com.bird.overlayer.data.local.models.Item
import com.bird.overlayer.extensions.toast
import com.bird.overlayer.ui.base.BaseActivity
import com.bird.overlayer.ui.common.CustomView
import com.bird.overlayer.utils.SaveAndShare
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import javax.inject.Inject

class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {

    @Inject
    lateinit var mainPresenter: MainPresenter
    private lateinit var photoContainer: RelativeLayout
    private lateinit var itemsAdapter: ItemsAdapter
    lateinit var mainCustomView: CustomView
    private var uri: Uri? = null

    override fun initUI() {
        mainCustomView = findViewById(R.id.mainCustomView)
        photoContainer = findViewById(R.id.photoContainer)
        itemsAdapter = ItemsAdapter {
            if (it.data != null)
                mainCustomView.setImageOverlay(
                    BitmapFactory.decodeByteArray(
                        it.data,
                        0,
                        it.data!!.size
                    )
                )
            else mainCustomView.clearOverlay()
        }
        recyclerViewItemList.adapter = itemsAdapter
        getMainBitmapUri()
        setMainBitmap()
    }


    override fun showItems(items: List<Item>) {
        runOnUiThread {
            itemsAdapter.items.addAll(items)
            itemsAdapter.notifyDataSetChanged()
        }
    }


    private fun setMainBitmap() {
        mainPresenter.prepareMainBitmap(uri, contentResolver)
    }

    private fun getMainBitmapUri() {
        if (intent?.hasExtra("img") == true)
            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                (intent.getParcelableExtra("img") as Uri?)
                    ?: Uri.fromFile(File(intent.getStringExtra("img") ?: ""))
            } else {
                Uri.fromFile(File(intent.getStringExtra("img") ?: ""))
            }
    }

    override fun showToast(title: String) {
        runOnUiThread {
            toast(title)
        }
    }

    override fun onMainBitmapReady(result: Bitmap) {
        val params = photoContainer.layoutParams as ConstraintLayout.LayoutParams
        params.dimensionRatio =
            (if (result.width < result.height) "W," else "H,") + result.width.toString() + ":" + result.height.toString()
        photoContainer.layoutParams = params
        photoContainer.post {
            mainCustomView.setImageBitmap(result)
        }
    }

    override fun initPresenter() = mainPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.activity_main

    fun saveImage(view: View) {
        SaveAndShare.save(this, mainPresenter.captureViewAsBitmap(photoContainer), null, null, null)
    }

    fun finish(view: View) {
        finish()
    }
}
