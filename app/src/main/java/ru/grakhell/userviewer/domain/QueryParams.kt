package ru.grakhell.userviewer.domain

data class QueryParams(
    var userName: String = "",
    var repositoryName: String = ""
) {
    override fun toString(): String {
        return "User name: $userName, and repository name: $repositoryName"
    }
}