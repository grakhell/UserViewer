package ru.grakhell.userviewer.storage.source

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.apollographql.apollo.api.Response
import com.crashlytics.android.Crashlytics
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import ru.grakhell.userviewer.domain.entity.GetWatchersInfoQuery
import ru.grakhell.userviewer.storage.remote.RxObservableCreator
import ru.grakhell.userviewer.util.RxUtil
import timber.log.Timber

class RepositoryWatchersInfoDataSource constructor(
    private val mUserName: String,
    private val mRepoName: String,
    private val mRepository: RxObservableCreator
)
    : PageKeyedDataSource<String, GetWatchersInfoQuery.Node>() {

    private lateinit var mResponse: Response<GetWatchersInfoQuery.Data>
    private lateinit var disposable: CompositeDisposable

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, GetWatchersInfoQuery.Node>
    ) {
        launch {
            try {
                disposable = CompositeDisposable()
                disposable.add(mRepository.getWatcherInfoObservable(mUserName,
                    params.requestedLoadSize, null, mRepoName)
                    .subscribe(
                        { x -> mResponse = checkNotNull(x)
                            if (!mResponse.hasErrors()) {
                                val items = checkNotNull(mResponse.data()
                                    ?.repository()
                                    ?.watchers()
                                    ?.edges()
                                    ?.mapNotNull { y -> y.node() }
                                    ?.toMutableList())
                                callback.onResult(
                                    items,
                                    mResponse.data()?.repository()?.watchers()?.pageInfo()?.startCursor(),
                                    mResponse.data()?.repository()?.watchers()?.pageInfo()?.endCursor()
                                )
                            } else {
                                mResponse.errors().forEach { y ->
                                    Timber.d(y.message())
                                    Crashlytics.log(y.message())
                                    y.customAttributes().forEach { z -> Crashlytics.log(z.toString()) }
                                }
                            } },
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
        callback: LoadCallback<String, GetWatchersInfoQuery.Node>
    ) {
        GlobalScope.launch {
            try {
                disposable.add(mRepository.getWatcherInfoObservable(mUserName,
                    params.requestedLoadSize, params.key, mRepoName)
                    .subscribe(
                        { x -> mResponse = checkNotNull(x)
                            if (!mResponse.hasErrors()) {
                                val items = checkNotNull(mResponse.data()
                                    ?.repository()
                                    ?.watchers()
                                    ?.edges()
                                    ?.mapNotNull { y -> y.node() }
                                    ?.toMutableList())
                                callback.onResult(
                                    items,
                                    mResponse.data()?.repository()?.watchers()?.pageInfo()?.endCursor())
                            } else {
                                mResponse.errors().forEach { y ->
                                    Timber.d(y.message())
                                    Crashlytics.log(y.message())
                                    y.customAttributes().forEach { z -> Crashlytics.log(z.toString()) }
                                }
                            } },
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
        callback: LoadCallback<String, GetWatchersInfoQuery.Node>
    ) {}

    override fun invalidate() {
        RxUtil.composedDispose(disposable)
        super.invalidate()
    }

    class DataSourceFactory(
        private val mUserName: String,
        private val mRepoName: String,
        private val mRepository: RxObservableCreator
    ) : DataSource.Factory<String, GetWatchersInfoQuery.Node>() {
        override fun create(): DataSource<String, GetWatchersInfoQuery.Node> {
            return RepositoryWatchersInfoDataSource(mUserName, mRepoName, mRepository)
        }
    }
}
