package ru.grakhell.userviewer.storage

import okhttp3.Credentials
import org.jetbrains.annotations.NotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Account @Inject constructor() {
    private lateinit var usr: String
    private lateinit var key: String

    fun setAccount(
        @NotNull name: String
    ) {
        usr = name
    }

    fun getAccount() = usr

    fun setKey(
        @NotNull token: String
    ) {
        key = Credentials.basic("bearer", token)
    }

    fun getKey() = key

    fun clearAccountInfo() {
        key = ""
        usr = ""
    }
}