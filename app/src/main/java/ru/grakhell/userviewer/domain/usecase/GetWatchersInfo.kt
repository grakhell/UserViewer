package ru.grakhell.userviewer.domain.usecase

import ru.grakhell.userviewer.domain.BaseUseCase
import ru.grakhell.userviewer.domain.QueryParams
import ru.grakhell.userviewer.storage.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetWatchersInfo @Inject constructor(rep: Repository) : BaseUseCase(rep) {

    override fun execute(params: QueryParams) =
        repo.getRepositoryWatchersData(params.userName, params.repositoryName)
}