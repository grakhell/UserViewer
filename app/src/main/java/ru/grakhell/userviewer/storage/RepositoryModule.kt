package ru.grakhell.userviewer.storage

import dagger.Module
import dagger.Provides
import ru.grakhell.userviewer.storage.remote.GitApiQueries
import ru.grakhell.userviewer.storage.remote.QueryManager
import ru.grakhell.userviewer.storage.remote.RxObservableCreator
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun rxObservableCreator(rxObservableCreator: RxObservableCreator): RxObservableCreator =
        rxObservableCreator
}