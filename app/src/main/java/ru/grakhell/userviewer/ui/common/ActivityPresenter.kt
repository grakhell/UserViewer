package ru.grakhell.userviewer.ui.common

import android.os.Bundle

interface ActivityPresenter {
    fun onStart(savedInstanceState: Bundle?)

    fun onEnd()

    fun onPause()

    fun onResume()

    fun onSaveInstanceState(outState: Bundle?)
}