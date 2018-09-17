package ru.grakhell.userviewer.auth

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AuthService: Service() {

    private lateinit var mAuthenticator: AppAuthenticator

    override fun onCreate() {

        mAuthenticator = AppAuthenticator(this)
        super.onCreate()
    }

    override fun onBind(p0: Intent?): IBinder?  = mAuthenticator.iBinder
}