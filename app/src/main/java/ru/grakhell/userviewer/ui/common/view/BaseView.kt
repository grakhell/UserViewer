package ru.grakhell.userviewer.ui.common.view

import android.os.Bundle
import ru.grakhell.userviewer.ui.common.presenter.Presenter
import javax.inject.Inject

abstract class BaseView<T: Presenter>: BaseFragment(), MVP {

    @Inject
    protected lateinit var presenter: T

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        presenter.onStart(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onDestroyView() {
        presenter.onEnd()
        super.onDestroyView()

    }
}