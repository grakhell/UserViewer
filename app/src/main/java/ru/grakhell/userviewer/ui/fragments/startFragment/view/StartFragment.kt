package ru.grakhell.userviewer.ui.fragments.startFragment.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.ui.common.view.BaseView
import ru.grakhell.userviewer.ui.fragments.startFragment.presenter.StartPresenter

class StartFragment : BaseView<StartPresenter>(), StartView {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }
}
