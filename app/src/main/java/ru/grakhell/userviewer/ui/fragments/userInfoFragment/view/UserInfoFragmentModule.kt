package ru.grakhell.userviewer.ui.fragments.userInfoFragment.view

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.common.view.BaseFragmentModule
import ru.grakhell.userviewer.ui.fragments.userInfoFragment.presenter.UserInfoPresenterModule
import javax.inject.Named

@Module(includes = [
    BaseFragmentModule::class,
    UserInfoPresenterModule::class])
abstract class UserInfoFragmentModule {

    @Binds
    @FragmentScope
    @Named(BaseFragmentModule.FRAGMENT)
    abstract fun fragment(userInfoFragment: UserInfoFragment):Fragment

    @Binds
    @FragmentScope
    abstract fun userInfoView(userInfoFragment: UserInfoFragment):UserInfoView
}