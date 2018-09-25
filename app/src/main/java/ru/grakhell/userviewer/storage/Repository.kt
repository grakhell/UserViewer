package ru.grakhell.userviewer.storage

import androidx.paging.PagedList
import io.reactivex.Observable
import org.jetbrains.annotations.NotNull
import ru.grakhell.userviewer.domain.entity.GetBranchesInfoQuery
import ru.grakhell.userviewer.domain.entity.GetLanguageInfoQuery
import ru.grakhell.userviewer.domain.entity.GetRepositoryInfoQuery
import ru.grakhell.userviewer.domain.entity.GetStargazersInfoQuery
import ru.grakhell.userviewer.domain.entity.GetUserInfoQuery
import ru.grakhell.userviewer.domain.entity.GetUserOrganisationInfoQuery
import ru.grakhell.userviewer.domain.entity.GetUserRepoInfoQuery
import ru.grakhell.userviewer.domain.entity.GetUserSearchResultQuery
import ru.grakhell.userviewer.domain.entity.GetUserStarredRepoInfoQuery
import ru.grakhell.userviewer.domain.entity.GetWatchersInfoQuery

interface Repository {
    fun getRepositoryBranchesData(
        @NotNull userName: String,
        @NotNull repoName: String
    ): Observable<PagedList<GetBranchesInfoQuery.Node>>

    fun getRepositoryLanguageData(
        @NotNull userName: String,
        @NotNull repoName: String
    ): Observable<PagedList<GetLanguageInfoQuery.Node>>

    fun getRepositoryStargazersData(
        @NotNull userName: String,
        @NotNull repoName: String
    ): Observable<PagedList<GetStargazersInfoQuery.Node>>

    fun getRepositoryWatchersData(
        @NotNull userName: String,
        @NotNull repoName: String
    ): Observable<PagedList<GetWatchersInfoQuery.Node>>

    fun getRepositoryData(
        @NotNull userName: String,
        @NotNull repoName: String
    ): Observable<GetRepositoryInfoQuery.Repository?>

    fun getUserSearchResultData(
        @NotNull userName: String
    ): Observable<PagedList<GetUserSearchResultQuery.AsUser>>

    fun getUserOrganisationData(
        @NotNull userName: String
    ): Observable<PagedList<GetUserOrganisationInfoQuery.Node>>

    fun getUserRepositoryData(
        @NotNull userName: String
    ): Observable<PagedList<GetUserRepoInfoQuery.Node>>

    fun getUserStarredRepositoryData(
        @NotNull userName: String
    ): Observable<PagedList<GetUserStarredRepoInfoQuery.Node>>

    fun getUserInfoData(
        @NotNull userName: String
    ): Observable<GetUserInfoQuery.User?>
}