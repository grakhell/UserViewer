package ru.grakhell.userviewer.storage.source

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.apollographql.apollo.api.Response
import com.crashlytics.android.Crashlytics
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import ru.grakhell.userviewer.domain.entity.GetUserStarredRepoInfoQuery
import ru.grakhell.userviewer.storage.remote.RxObservableCreator
import ru.grakhell.userviewer.util.RxUtil
import timber.log.Timber

class UserStarredRepositoriesInfoDataSource constructor(
    private val mName: String,
    private val mRepository: RxObservableCreator
)
    : PageKeyedDataSource<String, GetUserStarredRepoInfoQuery.Node>() {

    private lateinit var mResponse: Response<GetUserStarredRepoInfoQuery.Data>
    private lateinit var disposable: CompositeDisposable
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, GetUserStarredRepoInfoQuery.Node>
    ) {
        launch {
            try {
                disposable = CompositeDisposable()
                disposable.add(mRepository.getUserStarredRepoInfoObservable(mName,
                    params.requestedLoadSize, null).subscribe(
                    { x -> mResponse = checkNotNull(x)
                        if (!mResponse.hasErrors()) {
                            val items = checkNotNull(mResponse.data()
                                ?.user()
                                ?.starredRepositories()
                                ?.edges()
                                ?.mapNotNull { y -> y.node() }
                                ?.toMutableList())
                            callback.onResult(
                                items,
                                mResponse.data()?.user()?.starredRepositories()?.pageInfo()?.startCursor(),
                                mResponse.data()?.user()?.starredRepositories()?.pageInfo()?.endCursor()
                            )
                        } else {
                            mResponse.errors().forEach { y ->
                                Timber.d(y.message())
                                Crashlytics.log(y.message())
                                y.customAttributes().forEach { z -> Crashlytics.log(z.toString()) }
                            }
                        }
                    },
                    { ex ->
                        Timber.d(ex)
                        Crashlytics.logException(ex)
                    }))
            } catch (e: Exception) {
                Timber.d(e)
                Crashlytics.logException(e)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, GetUserStarredRepoInfoQuery.Node>
    ) {
        GlobalScope.launch {
            try {
                disposable.add(mRepository.getUserStarredRepoInfoObservable(mName,
                    params.requestedLoadSize, params.key)
                    .subscribe(
                    { x -> mResponse = checkNotNull(x)
                        if (!mResponse.hasErrors()) {
                            val items = checkNotNull(mResponse.data()
                                ?.user()
                                ?.starredRepositories()
                                ?.edges()
                                ?.mapNotNull { y -> y.node() }
                                ?.toMutableList())
                            callback.onResult(
                                items,
                                mResponse.data()?.user()?.starredRepositories()?.pageInfo()?.endCursor())
                        } else {
                            mResponse.errors().forEach { y ->
                                Timber.d(y.message())
                                Crashlytics.log(y.message())
                                y.customAttributes().forEach { z -> Crashlytics.log(z.toString()) }
                            }
                        }
                    },
                    { ex ->
                        Timber.d(ex)
                        Crashlytics.logException(ex)
                    }))
            } catch (e: Exception) {
                Timber.d(e)
                Crashlytics.logException(e)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, GetUserStarredRepoInfoQuery.Node>
    ) { }

    override fun invalidate() {
        RxUtil.composedDispose(disposable)
        super.invalidate()
    }

    class DataSourceFactory(
        private val mName: String,
        private val mRepository: RxObservableCreator
    ) : DataSource.Factory<String, GetUserStarredRepoInfoQuery.Node>() {
        override fun create(): DataSource<String, GetUserStarredRepoInfoQuery.Node> {
            return UserStarredRepositoriesInfoDataSource(mName, mRepository)
        }
    }
}