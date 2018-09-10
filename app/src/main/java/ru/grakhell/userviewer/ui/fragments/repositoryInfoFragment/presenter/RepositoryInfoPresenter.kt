package ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.presenter

import ru.grakhell.userviewer.ui.common.presenter.Presenter

interface RepositoryInfoPresenter:Presenter {
    fun setRepositoryInfo(ownerName:String?, repositoryName:String?)
}