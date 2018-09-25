package ru.grakhell.userviewer.util

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkUtil {
    companion object {
        fun isNetworkConnected(context: Context): Boolean {
            return runBlocking { val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager
                val activeNetworkInfo = cm.activeNetworkInfo
                return@runBlocking activeNetworkInfo != null &&
                    activeNetworkInfo.isConnected &&
                    isOnline().await() }
        }

        private fun isOnline() = async {
                try {
                    val timeoutMs = 1500
                    val sock = Socket()
                    val sockaddr = InetSocketAddress("8.8.8.8", 53)

                    sock.connect(sockaddr, timeoutMs)
                    sock.close()

                    return@async true
                } catch (e: IOException) {
                    return@async false
                }
            }
    }
}