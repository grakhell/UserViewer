package ru.grakhell.userviewer.ui.fragments.startFragment.presenter

import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.FragmentScope

@Module
abstract class StartPresenterModule {

    @Binds
    @FragmentScope
    abstract fun startPresenter(startPresenterImpl: StartPresenterImpl): StartPresenter
}