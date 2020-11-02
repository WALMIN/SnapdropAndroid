package com.walmin.android.snapdropandroid

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

class CustomWebChromeClient(val activity: Activity) : WebChromeClient() {

    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
        MainActivity.uploadMessage?.onReceiveValue(null)
        MainActivity.uploadMessage = null
        MainActivity.uploadMessage = filePathCallback

        try {
            activity.startActivityForResult(fileChooserParams!!.createIntent(), MainActivity.REQUEST_SELECT_FILE)

        } catch (e: ActivityNotFoundException) {
            MainActivity.uploadMessage = null
            Tools.showSnackbar(MainActivity.layout, activity.getString(R.string.errorOpeningChooser))

            return false

        }
        return true

    }

}