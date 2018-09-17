package ru.grakhell.userviewer.storage.remote

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueryManager @Inject constructor() : GitApiQueries {

    override fun getUserSearchResult(name: String, count: Int, cursor: String?):
        ApolloCall<GetUserSearchResultQuery.Data> {
            return GitHubAPIService.getClient().query(
                GetUserSearchResultQuery.builder()
                .owner_name(name)
                .count(count)
                .cursor(cursor)
                .build())
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
        }

    override fun getUserOrganisationInfo(name: String, count: Int, cursor: String?):
        ApolloCall<GetUserOrganisationInfoQuery.Data> {
            return GitHubAPIService.getClient().query(
                GetUserOrganisationInfoQuery.builder()
                    .user_name(name)
                    .count(count)
                    .cursor(cursor)
                    .build())
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
        }

    override fun getUserStarredRepoInfo(name: String, count: Int, cursor: String?):
        ApolloCall<GetUserStarredRepoInfoQuery.Data> {
            return GitHubAPIService.getClient().query(
                GetUserStarredRepoInfoQuery.builder()
                    .user_name(name)
                    .count(count)
                    .cursor(cursor)
                    .build())
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
        }

    override fun getUserRepoInfo(name: String, count: Int, cursor: String?):
        ApolloCall<GetUserRepoInfoQuery.Data> {
            return GitHubAPIService.getClient().query(
                GetUserRepoInfoQuery.builder()
                    .user_name(name)
                    .count(count)
                    .cursor(cursor)
                    .build())
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
        }

    override fun getUserInfo(name: String):
        ApolloCall<GetUserInfoQuery.Data> {
        return GitHubAPIService.getClient().query(
            GetUserInfoQuery.builder()
                .owner_name(name)
                .build())
            .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
    }

    override fun getBranchesInfo(name: String, count: Int, cursor: String?, repositoryName: String):
        ApolloCall<GetBranchesInfoQuery.Data> {
            return GitHubAPIService.getClient().query(
                GetBranchesInfoQuery.builder()
                    .owner_name(name)
                    .repo_name(repositoryName)
                    .count(count)
                    .cursor(cursor)
                    .build())
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
        }

    override fun getLanguageInfo(name: String, count: Int, cursor: String?, repositoryName: String):
        ApolloCall<GetLanguageInfoQuery.Data> {
            return GitHubAPIService.getClient().query(
                GetLanguageInfoQuery.builder()
                    .owner_name(name)
                    .repo_name(repositoryName)
                    .count(count)
                    .cursor(cursor)
                    .build())
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
        }

    override fun getRepositoryInfo(name: String, repositoryName: String):
        ApolloCall<GetRepositoryInfoQuery.Data> {
            return GitHubAPIService.getClient().query(
                GetRepositoryInfoQuery.builder()
                    .owner_name(name)
                    .repo_name(repositoryName)
                    .build())
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
        }

    override fun getStargazesInfo(name: String, count: Int, cursor: String?, repositoryName: String):
        ApolloCall<GetStargazersInfoQuery.Data> {
            return GitHubAPIService.getClient().query(
                GetStargazersInfoQuery.builder()
                    .owner_name(name)
                    .repo_name(repositoryName)
                    .count(count)
                    .cursor(cursor)
                    .build())
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
        }

    override fun getWatcherInfo(name: String, count: Int, cursor: String?, repositoryName: String):
        ApolloCall<GetWatchersInfoQuery.Data> {
            return GitHubAPIService.getClient().query(
                GetWatchersInfoQuery.builder()
                    .owner_name(name)
                    .repo_name(repositoryName)
                    .count(count)
                    .cursor(cursor)
                    .build())
                .responseFetcher(ApolloResponseFetchers.CACHE_FIRST)
        }
}