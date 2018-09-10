package ru.grakhell.userviewer.ui.fragments.searchResultFragment.presenter

import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.FragmentScope

@Module
abstract class SearchResultPresenterModule {

    @Binds
    @FragmentScope
    abstract fun searchResultPresenter(searchResultPresenterImpl: SearchResultPresenterImpl)
        :SearchResultPresenter
}