package ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.common.view.BaseFragmentModule
import ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.presenter.RepositoryInfoPresenterModule
import javax.inject.Named

@Module(includes = [
    BaseFragmentModule::class,
    RepositoryInfoPresenterModule::class])
abstract class RepositoryInfoFragmentModule {
    @Binds
    @FragmentScope
    @Named(BaseFragmentModule.FRAGMENT)
    abstract fun fragment(repositoryInfoFragment: RepositoryInfoFragment):Fragment

    @Binds
    @FragmentScope
    abstract fun repositoryInfoView(repositoryInfoFragment: RepositoryInfoFragment):RepositoryInfoView
}