package com.bird.overlayer.ui.components.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.bird.overlayer.R
import com.bird.overlayer.extensions.launchActivity
import com.bird.overlayer.extensions.withDelay
import com.bird.overlayer.ui.base.BaseActivity
import com.bird.overlayer.ui.components.main.MainActivity
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import javax.inject.Inject


class SplashActivity : BaseActivity<SplashContract.View, SplashContract.Presenter>(),
    SplashContract.View {

    @Inject
    lateinit var splashPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
    }

    override fun initUI() {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, GALLERY_REQ)) {
            val images: ArrayList<Image> = ImagePicker.getImages(data)
            for (image in images) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.putExtra("img", image.uri)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.putExtra("img", image.path)
                    startActivity(intent)
                }
            }
        }
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
    }

    override fun initPresenter() = splashPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.activity_splash

    fun selectPhoto(view: View) {
        ImagePicker.with(this)
            .setFolderMode(true)
            .setShowCamera(false)
            .setFolderTitle("Select Image")
            .setRootDirectoryName(ROOT_DIR_DCIM)
            .setDirectoryName("Overlay Shoot")
            .setMultipleMode(false)
            .setRequestCode(GALLERY_REQ)
            .start()
    }

    companion object {
        const val GALLERY_REQ = 143
        val ROOT_DIR_DCIM: String = Environment.DIRECTORY_DCIM
        val ROOT_DIR_DOWNLOAD: String = Environment.DIRECTORY_DOWNLOADS
        val ROOT_DIR_PICTURES: String = Environment.DIRECTORY_PICTURES
    }

}