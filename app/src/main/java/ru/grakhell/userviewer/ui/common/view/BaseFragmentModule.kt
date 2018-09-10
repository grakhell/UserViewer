package ru.grakhell.userviewer.ui.common.view

import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides
import ru.grakhell.userviewer.injection.scope.FragmentScope
import javax.inject.Named

@Module
abstract class BaseFragmentModule {
    @Module
    companion object {
        const val FRAGMENT = "BaseFragmentModule.fragment"
        const val CHILD_FRAGMENT_MANAGER = "BAseFragmentModule.childFragmentManager"

        @Provides
        @Named(CHILD_FRAGMENT_MANAGER)
        @FragmentScope
        @JvmStatic
        fun childFragmentManager(@Named(FRAGMENT) fragment: Fragment) = fragment.childFragmentManager

    }
}