package ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.presenter

import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.FragmentScope

@Module
abstract class RepositoryInfoPresenterModule {
    @Binds
    @FragmentScope
    abstract fun RepositoryInfoPresenter(repositoryInfoPresenterImpl: RepositoryInfoPresenterImpl): RepositoryInfoPresenter
}