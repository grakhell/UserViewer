package ru.grakhell.userviewer.ui.common

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.grakhell.userviewer.injection.scope.ActivityScope
import javax.inject.Named

@Module
abstract class BaseActivityModule {

    @ActivityScope
    @Binds
    abstract fun activityContext(activity: AppCompatActivity): Context

    @Module
    companion object {
        const val ACTIVITY_FRAGMENT_MANAGER = "baseActivityModule.activityFragmentManager"

        @JvmStatic
        @Provides
        @Named(ACTIVITY_FRAGMENT_MANAGER)
        @ActivityScope
        fun activitySupportFragmentManager(activity: AppCompatActivity): FragmentManager =
            activity.supportFragmentManager
    }
}