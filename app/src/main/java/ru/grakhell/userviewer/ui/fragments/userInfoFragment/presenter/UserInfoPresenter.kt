package ru.grakhell.userviewer.ui.fragments.userInfoFragment.presenter

import ru.grakhell.userviewer.ui.common.presenter.Presenter

interface UserInfoPresenter:Presenter {

    fun setUserName(user:String?)
}