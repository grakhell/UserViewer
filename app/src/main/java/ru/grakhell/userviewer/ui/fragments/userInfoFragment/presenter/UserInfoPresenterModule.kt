package ru.grakhell.userviewer.ui.fragments.userInfoFragment.presenter

import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.FragmentScope

@Module
abstract class UserInfoPresenterModule {

    @Binds
    @FragmentScope
    abstract fun userInfoPresenter(userInfoPresenterImpl: UserInfoPresenterImpl):UserInfoPresenter
}