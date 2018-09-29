package ru.grakhell.userviewer.ui.common

import android.os.Bundle

abstract class BaseActivityPresenter<T : Activity>(protected var mActivity: T?) : ActivityPresenter {

    override fun onStart(savedInstanceState: Bundle?) {}

    override fun onEnd() {}

    override fun onPause() {}

    override fun onResume() {}

    override fun onSaveInstanceState(outState: Bundle?) {}
}