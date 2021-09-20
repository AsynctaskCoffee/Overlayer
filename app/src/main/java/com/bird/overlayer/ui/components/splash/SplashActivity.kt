package com.bird.overlayer.ui.components.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.bird.overlayer.R
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

    private lateinit var infoText: TextView
    private lateinit var loadingContainer: LinearLayout
    private lateinit var filterBtn: LinearLayout
    private lateinit var progressBar: ProgressBar
    private var progresser: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
    }

    override fun initUI() {
        infoText = findViewById(R.id.infoText)
        loadingContainer = findViewById(R.id.loadingContainer)
        filterBtn = findViewById(R.id.filterBtn)
        progressBar = findViewById(R.id.progressBar)
    }

    override fun showToast(title: String) {
        runOnUiThread {
            showToast(title)
        }
    }

    override fun showProgress() {
        runOnUiThread {
            progresser++
            progressBar.progress = progresser
        }
    }

    override fun hideProgress() {

    }

    override fun showProgressWithTitle(title: String) {
        runOnUiThread {
            progresser++
            progressBar.progress = progresser
        }
    }

    override fun onDataReady() {
        runOnUiThread {
            progresser = 5
            progressBar.progress = progresser
            withDelay(500) {
                loadingContainer.visibility = View.GONE
                filterBtn.visibility = View.VISIBLE
            }
        }
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