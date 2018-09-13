package ru.grakhell.userviewer.ui.fragments.startFragment.presenter

import ru.grakhell.userviewer.ui.common.presenter.BasePresenter
import ru.grakhell.userviewer.ui.fragments.startFragment.view.StartView
import javax.inject.Inject

class StartPresenterImpl @Inject constructor(
    view: StartView?
):BasePresenter<StartView>(view), StartPresenter {


}