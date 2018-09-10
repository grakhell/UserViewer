package ru.grakhell.userviewer.ui.fragments.searchResultFragment.view

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.common.view.BaseFragmentModule
import ru.grakhell.userviewer.ui.fragments.searchResultFragment.presenter.SearchResultPresenterModule
import javax.inject.Named

@Module(includes = [
    BaseFragmentModule::class,
    SearchResultPresenterModule::class])
abstract class SearchResultFragmentModule {

    @Binds
    @FragmentScope
    @Named(BaseFragmentModule.FRAGMENT)
    abstract fun fragment(searchResultFragment: SearchResultFragment): Fragment

    @Binds
    @FragmentScope
    abstract fun searchResultView(searchResultFragment: SearchResultFragment):SearchResultView
}