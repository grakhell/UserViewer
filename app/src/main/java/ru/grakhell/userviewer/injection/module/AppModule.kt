package ru.grakhell.userviewer.injection.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.grakhell.userviewer.injection.scope.ActivityScope
import ru.grakhell.userviewer.storage.Repository
import ru.grakhell.userviewer.storage.RepositoryImpl
import ru.grakhell.userviewer.storage.remote.GitApiQueries
import ru.grakhell.userviewer.storage.remote.QueryManager
import ru.grakhell.userviewer.ui.activity.view.ConductorActivity
import ru.grakhell.userviewer.ui.activity.view.ConductorActivityModule
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun application(application: Application): Application

    @Binds
    @Singleton
    abstract fun appContext(application: Application): Context

    @Binds
    @Singleton
    abstract fun repository(repositoryImpl: RepositoryImpl): Repository

    @Binds
    @Singleton
    abstract fun queryManager(queryManager: QueryManager): GitApiQueries

    @ActivityScope
    @ContributesAndroidInjector(modules = [ConductorActivityModule::class])
    abstract fun conductorActivity(): ConductorActivity
}