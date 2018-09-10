package ru.grakhell.userviewer.ui.common.view

import dagger.Module

@Module
abstract class BaseChildFragmentModule {
    companion object {
        const val CHILD_FRAGMENT = "BaseChildFragmentModule.childFragment"
    }
}