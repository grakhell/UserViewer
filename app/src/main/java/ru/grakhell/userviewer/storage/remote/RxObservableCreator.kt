package ru.grakhell.userviewer.storage.remote

import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
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
class RxObservableCreator @Inject constructor(private val gitApi: GitApiQueries) {

    fun getUserSearchResultObservable(
        name: String,
        count: Int,
        cursor: String?
    ):
        Observable<Response<GetUserSearchResultQuery.Data>> {
        return Rx2Apollo.from(gitApi.getUserSearchResult(name, count, cursor))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
    }

    fun getUserOrganisationInfoObservable(
        name: String,
        count: Int,
        cursor: String?
    ):
        Observable<Response<GetUserOrganisationInfoQuery.Data>> =
        Rx2Apollo.from(gitApi.getUserOrganisationInfo(name, count, cursor))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())

    fun getUserStarredRepoInfoObservable(
        name: String,
        count: Int,
        cursor: String?
    ):
        Observable<Response<GetUserStarredRepoInfoQuery.Data>> =
        Rx2Apollo.from(gitApi.getUserStarredRepoInfo(name, count, cursor))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())

    fun getUserRepoInfoObservable(
        name: String,
        count: Int,
        cursor: String?
    ):
        Observable<Response<GetUserRepoInfoQuery.Data>> =
        Rx2Apollo.from(gitApi.getUserRepoInfo(name, count, cursor))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())

    fun getUserInfoObservable(
        name: String
    ):
        Observable<Response<GetUserInfoQuery.Data>> =
        Rx2Apollo.from(gitApi.getUserInfo(name))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())

    fun getBranchesInfoObservable(
        name: String,
        count: Int,
        cursor: String?,
        repositoryName: String
    ):
        Observable<Response<GetBranchesInfoQuery.Data>> =
        Rx2Apollo.from(gitApi.getBranchesInfo(name, count, cursor, repositoryName))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())

    fun getLanguageInfoObservable(
        name: String,
        count: Int,
        cursor: String?,
        repositoryName: String
    ):
        Observable<Response<GetLanguageInfoQuery.Data>> =
        Rx2Apollo.from(gitApi.getLanguageInfo(name, count, cursor, repositoryName))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())

    fun getRepositoryInfoObservable(
        name: String,
        repositoryName: String
    ):
        Observable<Response<GetRepositoryInfoQuery.Data>> =
        Rx2Apollo.from(gitApi.getRepositoryInfo(name, repositoryName))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())

    fun getStargazesInfoObservable(
        name: String,
        count: Int,
        cursor: String?,
        repositoryName: String
    ):
        Observable<Response<GetStargazersInfoQuery.Data>> =
        Rx2Apollo.from(gitApi.getStargazesInfo(name, count, cursor, repositoryName))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())

    fun getWatcherInfoObservable(
        name: String,
        count: Int,
        cursor: String?,
        repositoryName: String
    ):
        Observable<Response<GetWatchersInfoQuery.Data>> =
        Rx2Apollo.from(gitApi.getWatcherInfo(name, count, cursor, repositoryName))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
}