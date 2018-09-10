package ru.grakhell.userviewer.storage.remote

import com.apollographql.apollo.ApolloCall
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

interface GitApiQueries {

    //User information related queries
    fun getUserSearchResult(
        @NotNull name:String,
        @NotNull count:Int,
        cursor:String?):
        ApolloCall<GetUserSearchResultQuery.Data>

    fun getUserOrganisationInfo(
        @NotNull name:String,
        @NotNull count:Int,
        cursor:String?):
        ApolloCall<GetUserOrganisationInfoQuery.Data>

    fun getUserStarredRepoInfo(
        @NotNull name:String,
        @NotNull count:Int,
        cursor:String?):
        ApolloCall<GetUserStarredRepoInfoQuery.Data>

    fun getUserRepoInfo(
        @NotNull name:String,
        @NotNull count:Int,
        cursor:String?):
        ApolloCall<GetUserRepoInfoQuery.Data>

    fun getUserInfo(
        @NotNull name:String
    ):ApolloCall<GetUserInfoQuery.Data>

    //Repository Information related queries
    fun getBranchesInfo(name:String, count:Int, cursor:String?, repositoryName:String):
        ApolloCall<GetBranchesInfoQuery.Data>

    fun getLanguageInfo(name:String, count:Int, cursor:String?, repositoryName:String):
        ApolloCall<GetLanguageInfoQuery.Data>

    fun getRepositoryInfo(name:String,  repositoryName:String):
        ApolloCall<GetRepositoryInfoQuery.Data>

    fun getStargazesInfo(name:String, count:Int, cursor:String?, repositoryName:String):
        ApolloCall<GetStargazersInfoQuery.Data>

    fun getWatcherInfo(name:String, count:Int, cursor:String?, repositoryName:String):
        ApolloCall<GetWatchersInfoQuery.Data>
}