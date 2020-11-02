package com.walmin.android.snapdropandroid

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.*

class CustomWebViewClient(val context: Context) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest?): Boolean {
        if(Uri.parse(request?.url.toString()).host?.contains(context.resources.getString(R.string.snapdropURL))!!) {
            return false
        }

        // Open external app
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(request?.url.toString())))
        return true

    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if(Uri.parse(url).host?.contains(context.resources.getString(R.string.snapdropURL))!!) {
            return false
        }

        // Open external app
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        return true

    }

    // Page loaded
    override fun onPageFinished(view: WebView, url: String?) {
        super.onPageFinished(view, url)

        MainActivity.snapdropRefreshView.isRefreshing = false

        // Show error page if there is no internet
        if(!Tools.isNetworkAvailable(context)){
            Tools.showError(view)

        }

    }

    // Show error page
    override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
        Tools.showError(view)

    }

    // Show error page
    override fun onReceivedHttpError(view: WebView, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
        Tools.showError(view)
        super.onReceivedHttpError(view, request, errorResponse)

    }

    // Show error page
    @Suppress("DEPRECATION")
    override fun onReceivedError(view: WebView, errorCode: Int, description: String?, failingUrl: String?) {
        Tools.showError(view)
        super.onReceivedError(view, errorCode, description, failingUrl)

    }

}