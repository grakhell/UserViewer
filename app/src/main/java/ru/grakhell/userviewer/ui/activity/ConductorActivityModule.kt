package ru.grakhell.userviewer.ui.activity

import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.grakhell.userviewer.injection.scope.ActivityScope
import ru.grakhell.userviewer.injection.scope.FragmentScope
import ru.grakhell.userviewer.ui.common.BaseActivityModule
import ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view.RepositoryInfoFragment
import ru.grakhell.userviewer.ui.fragments.repositoryInfoFragment.view.RepositoryInfoFragmentModule
import ru.grakhell.userviewer.ui.fragments.searchResultFragment.view.SearchResultFragment
import ru.grakhell.userviewer.ui.fragments.searchResultFragment.view.SearchResultFragmentModule
import ru.grakhell.userviewer.ui.fragments.userInfoFragment.view.UserInfoFragment
import ru.grakhell.userviewer.ui.fragments.userInfoFragment.view.UserInfoFragmentModule

@Module(includes = [BaseActivityModule::class])
abstract class ConductorActivityModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [SearchResultFragmentModule::class])
    abstract fun searchResultFragment(): SearchResultFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [UserInfoFragmentModule::class])
    abstract fun userInfoFragment(): UserInfoFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [RepositoryInfoFragmentModule::class])
    abstract fun repositoryInfoFragment(): RepositoryInfoFragment

    @ActivityScope
    @Binds
    abstract fun activity(activity: ConductorActivity):AppCompatActivity
}