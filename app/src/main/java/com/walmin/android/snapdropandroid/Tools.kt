package com.walmin.android.snapdropandroid

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

object Tools {

    // Clear webview data
    private fun clearData(context: Context, view: WebView){
        context.deleteDatabase("webview.db")
        context.deleteDatabase("webviewCache.db")

        view.clearCache(true)
        view.clearHistory()

    }

    // Show error page
    fun showError(view: WebView){
        clearData(view.context, view)
        view.stopLoading()

        view.loadUrl(view.context.resources.getString(R.string.errorPageURL))

    }

    // Show snackbar
    fun showSnackbar(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

    }

    // Check network
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null){
            return false

        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> { return true }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> { return true }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> { return true }
                }

            }

        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }

        }
        return false

    }

}