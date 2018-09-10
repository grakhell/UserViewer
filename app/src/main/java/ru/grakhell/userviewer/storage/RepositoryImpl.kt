package ru.grakhell.userviewer.storage

import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.grakhell.userviewer.domain.entity.GetRepositoryInfoQuery
import ru.grakhell.userviewer.domain.entity.GetUserInfoQuery
import ru.grakhell.userviewer.storage.remote.RxObservableCreator
import ru.grakhell.userviewer.storage.source.RepositoryBranchesInfoDataSource
import ru.grakhell.userviewer.storage.source.RepositoryInfoSource
import ru.grakhell.userviewer.storage.source.RepositoryLangInfoDataSource
import ru.grakhell.userviewer.storage.source.RepositoryStargazersInfoDateSource
import ru.grakhell.userviewer.storage.source.RepositoryWatchersInfoDataSource
import ru.grakhell.userviewer.storage.source.UserInfoSource
import ru.grakhell.userviewer.storage.source.UserOrganisationInfoDataSource
import ru.grakhell.userviewer.storage.source.UserRepositoryInfoDataSource
import ru.grakhell.userviewer.storage.source.UserSearchResultDataSource
import ru.grakhell.userviewer.storage.source.UserStarredRepositoriesInfoDataSource
import javax.inject.Inject

@Suppress("NOTHING_TO_INLINE")
class RepositoryImpl @Inject constructor(private val rxBuilder:RxObservableCreator): Repository {

    private val PAGE_SIZE:Int = 20
    private val config:PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(PAGE_SIZE *2)
        .setPageSize(PAGE_SIZE)
        .build()

    override fun getRepositoryBranchesData(userName:String, repoName:String) =
    RepositoryBranchesInfoDataSource.DataSourceFactory(userName,repoName, rxBuilder)
            .getRxObservable(config)

    override fun getRepositoryLanguageData(userName:String, repoName:String) =
        RepositoryLangInfoDataSource.DataSourceFactory(userName,repoName, rxBuilder)
            .getRxObservable(config)

    override fun getRepositoryStargazersData(userName:String, repoName:String) =
        RepositoryStargazersInfoDateSource.DataSourceFactory(userName,repoName, rxBuilder)
            .getRxObservable(config)

    override fun getRepositoryWatchersData(userName:String, repoName:String) =
        RepositoryWatchersInfoDataSource.DataSourceFactory(userName,repoName, rxBuilder)
            .getRxObservable(config)

     override fun getRepositoryData(userName:String,repoName:String)
         :Observable<GetRepositoryInfoQuery.Repository?> =
         RepositoryInfoSource(userName, repoName, rxBuilder).parse()

    override fun getUserSearchResultData(userName:String) =
        UserSearchResultDataSource.DataSourceFactory(userName, rxBuilder)
            .getRxObservable(config)

    override fun getUserOrganisationData(userName: String) =
        UserOrganisationInfoDataSource.DataSourceFactory(userName, rxBuilder)
            .getRxObservable(config)

    override fun getUserRepositoryData(userName: String) =
        UserRepositoryInfoDataSource.DataSourceFactory(userName, rxBuilder)
            .getRxObservable(config)

    override fun getUserStarredRepositoryData(userName: String) =
        UserStarredRepositoriesInfoDataSource.DataSourceFactory(userName, rxBuilder)
            .getRxObservable(config)

    override fun getUserInfoData(userName: String): Observable<GetUserInfoQuery.User?> =
        UserInfoSource(userName, rxBuilder).parse()


    private inline fun <K, V> DataSource.Factory<K, V>.getRxObservable(
        config: PagedList.Config
    ) = RxPagedListBuilder(this,config)
        .setFetchScheduler(Schedulers.computation())
        .setNotifyScheduler(AndroidSchedulers.mainThread())
        .buildObservable()
}