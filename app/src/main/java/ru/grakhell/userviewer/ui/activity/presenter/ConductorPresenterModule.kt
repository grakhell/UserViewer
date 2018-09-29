package ru.grakhell.userviewer.ui.activity.presenter

import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.ActivityScope
import ru.grakhell.userviewer.injection.scope.FragmentScope

@Module
abstract class ConductorPresenterModule {

    @Binds
    @ActivityScope
    abstract fun conductorPresenter(startPresenterImpl: ConductorPresenterImpl): ConductorPresenter

    @Binds
    @ActivityScope
    abstract fun onTokenAcquired(onTokenAcquired: OnTokenAcquired):OnTokenAcquired
}