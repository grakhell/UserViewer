package ru.grakhell.userviewer.ui.common.presenter

import android.os.Bundle
import ru.grakhell.userviewer.ui.common.view.MVP

abstract class BasePresenter<T : MVP>(protected var mView: T?) : Presenter {

    override fun onStart(savedInstanceState: Bundle?) {}

    override fun onEnd() {}

    override fun onPause() {}

    override fun onResume() {}

    override fun onSaveInstanceState(outState: Bundle?) {}
}