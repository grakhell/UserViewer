package ru.grakhell.userviewer.ui.common.presenter

import android.os.Bundle

interface Presenter {

    fun onStart(savedInstanceState: Bundle?)

    fun onResume()

    fun onPause()

    fun onSaveInstanceState(outState: Bundle?)

    fun onEnd()
    
}