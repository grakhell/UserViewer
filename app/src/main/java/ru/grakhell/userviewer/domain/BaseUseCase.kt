package ru.grakhell.userviewer.domain

import ru.grakhell.userviewer.storage.Repository

abstract class BaseUseCase(protected val repo: Repository) {
    abstract fun execute(params: QueryParams): Any
}