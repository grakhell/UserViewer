package ru.grakhell.userviewer.ui.fragments.startFragment.view

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.common.view.BaseFragmentModule
import ru.grakhell.userviewer.ui.fragments.startFragment.presenter.StartPresenterModule
import javax.inject.Named

@Module(includes = [
    BaseFragmentModule::class,
    StartPresenterModule::class])
abstract class StartFragmentModule {

    @Binds
    @FragmentScope
    @Named(BaseFragmentModule.FRAGMENT)
    abstract fun fragment(startFragment: StartFragment): Fragment

    @Binds
    @FragmentScope
    abstract fun startView(startFragment: StartFragment): StartView
}