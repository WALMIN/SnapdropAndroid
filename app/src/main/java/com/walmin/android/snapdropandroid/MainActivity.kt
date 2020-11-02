package com.walmin.android.snapdropandroid

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature

class MainActivity : AppCompatActivity(){

    companion object MainActivity {
        // Views
        lateinit var layout: ConstraintLayout
        lateinit var snapdropRefreshView: SwipeRefreshLayout

        // Upload file
        var mUploadMessage: ValueCallback<Uri>? = null
        var uploadMessage: ValueCallback<Array<Uri>>? = null
        const val FILECHOOSER_RESULTCODE = 1
        const val REQUEST_SELECT_FILE = 100

    }

    // Views
    lateinit var snapdropView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()

        // Load Snapdrop or error page based on network
        if(Tools.isNetworkAvailable(this)){
            snapdropView.loadUrl(getString(R.string.snapdropURL))

        }else{
            Tools.showError(snapdropView)

        }

    }

    @Suppress("DEPRECATION")
    fun initialize(){
        // Views
        layout = findViewById(R.id.layout)

        // Refresh
        snapdropRefreshView = findViewById(R.id.snapdropRefresh)
            snapdropRefreshView.setOnRefreshListener {
                if(Tools.isNetworkAvailable(this)){
                    snapdropView.loadUrl(getString(R.string.snapdropURL))

                }else{
                    Tools.showError(snapdropView)

                }

            }

        // Snapdrop
        snapdropView = findViewById(R.id.snapdrop)
            snapdropView.webViewClient = CustomWebViewClient(this)
            snapdropView.webChromeClient = CustomWebChromeClient(this)

            // Websettings
            snapdropView.settings.setSupportZoom(false)
            snapdropView.settings.displayZoomControls = false
            snapdropView.settings.builtInZoomControls = false
            snapdropView.settings.javaScriptEnabled = true
            snapdropView.settings.allowFileAccess = true
            snapdropView.settings.allowContentAccess = true
            snapdropView.settings.allowFileAccessFromFileURLs = true
            snapdropView.settings.allowUniversalAccessFromFileURLs = true

            // Download file
            snapdropView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                AlertDialog.Builder(this@MainActivity)
                    .setTitle(getString(R.string.noDownloadFeatureTitle))
                    .setMessage(getString(R.string.noDownloadFeature))
                    .setPositiveButton(getString(R.string.close)) { dialog, which -> }
                    .setNegativeButton(getString(R.string.browser)) { dialog, which ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.snapdropURL))))

                    }
                    .show()

                // TODO - Make files downloadable

            }

            // Set theme
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        WebSettingsCompat.setForceDark(snapdropView.settings, WebSettingsCompat.FORCE_DARK_OFF)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        WebSettingsCompat.setForceDark(snapdropView.settings, WebSettingsCompat.FORCE_DARK_ON)
                    }
                    else -> {
                        WebSettingsCompat.setForceDark(snapdropView.settings, WebSettingsCompat.FORCE_DARK_AUTO)
                    }

                }

            }

    }

    // Upload file
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_SELECT_FILE -> {
                if (uploadMessage == null) {
                    return

                }
                uploadMessage?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
                uploadMessage = null

            }
            FILECHOOSER_RESULTCODE -> {
                if (mUploadMessage == null) {
                    return

                }
                mUploadMessage?.onReceiveValue(if (intent == null || resultCode != RESULT_OK) null else intent.data)
                mUploadMessage = null

            }
            else -> {
                Tools.showSnackbar(layout, getString(R.string.uploadFailed))

            }

        }

    }

}