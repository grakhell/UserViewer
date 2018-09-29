package ru.grakhell.userviewer.ui.fragments.startFragment.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.ui.activity.view.ConductorActivity
import ru.grakhell.userviewer.ui.common.view.BaseView
import ru.grakhell.userviewer.ui.activity.presenter.ConductorPresenter
import javax.inject.Inject


class StartFragment @Inject constructor() : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

}
