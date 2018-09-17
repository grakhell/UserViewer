package ru.grakhell.oauthlib.ui

import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient

class AuthViewModel : ViewModel() {

    var isAddingNewAccount: Boolean = false
    var isGetToken: Boolean = false

    var userName:String = ""
    var userKey:String = ""
    var userToken:String = ""

    private val HEADER_2FA = ":2fa-type"

    private val BASE_URL: String = "https://api.github.com"
    private val client = OkHttpClient.Builder()
        .build()

}
