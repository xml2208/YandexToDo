package com.xml.yandextodo.presentation.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun isInternetAvailable(): Boolean {
    val context = LocalContext.current
    var isInternetAvailable by remember { mutableStateOf(false) }

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkCapabilities = connectivityManager.activeNetwork?.let {
        connectivityManager.getNetworkCapabilities(it)
    }

    isInternetAvailable = networkCapabilities?.let {
        it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } ?: false

    return isInternetAvailable
}