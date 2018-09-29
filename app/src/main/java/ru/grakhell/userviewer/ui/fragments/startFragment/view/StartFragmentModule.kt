package ru.grakhell.userviewer.ui.fragments.startFragment.view

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.common.view.BaseFragmentModule
import ru.grakhell.userviewer.ui.activity.presenter.ConductorPresenterModule
import javax.inject.Named

@Module
abstract class StartFragmentModule {

    @Binds
    @FragmentScope
    abstract fun fragment(startFragment: StartFragment): Fragment
}