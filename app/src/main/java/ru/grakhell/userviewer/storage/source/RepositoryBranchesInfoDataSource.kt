package ru.grakhell.userviewer.storage.source

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.apollographql.apollo.api.Response
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.launch
import ru.grakhell.userviewer.domain.entity.GetBranchesInfoQuery
import ru.grakhell.userviewer.storage.remote.RxObservableCreator
import ru.grakhell.userviewer.util.RxUtil
import timber.log.Timber

class RepositoryBranchesInfoDataSource constructor(
    private val mUserName:String,
    private val mRepoName:String,
    private val mRepository: RxObservableCreator)
    : PageKeyedDataSource<String, GetBranchesInfoQuery.Node>() {


    private lateinit var mResponse: Response<GetBranchesInfoQuery.Data>
    private val disposable:CompositeDisposable = CompositeDisposable()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, GetBranchesInfoQuery.Node>
    ) {
        launch {
            try {
                disposable.add(mRepository.getBranchesInfoObservable(mUserName, params
                    .requestedLoadSize, null,mRepoName)
                    .subscribe(
                    { x -> mResponse = checkNotNull(x)
                        if (!mResponse.hasErrors()) {
                            val items = checkNotNull(mResponse.data()
                                ?.repository()
                                ?.refs()
                                ?.edges()
                                ?.mapNotNull { y -> y.node() }
                                ?.toMutableList())
                            callback.onResult(
                                items,
                                mResponse.data()?.repository()?.refs()?.pageInfo()?.startCursor(),
                                mResponse.data()?.repository()?.refs()?.pageInfo()?.endCursor()
                            )
                        } else {
                            mResponse.errors().forEach { y -> Timber.d(y.message()) }
                        }},
                    { ex -> Timber.d(ex) }))
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, GetBranchesInfoQuery.Node>
    ) {
        launch {
            try {
                disposable.add(mRepository.getBranchesInfoObservable(mUserName, params
                    .requestedLoadSize, params.key,mRepoName)
                    .subscribe(
                        { x -> mResponse = checkNotNull(x)
                            if (!mResponse.hasErrors()) {
                                val items = checkNotNull(mResponse.data()
                                    ?.repository()
                                    ?.refs()
                                    ?.edges()
                                    ?.mapNotNull { y -> y.node() }
                                    ?.toMutableList())
                                callback.onResult(
                                    items,
                                    mResponse.data()?.repository()?.refs()?.pageInfo()?.endCursor())
                            } else {
                                mResponse.errors().forEach { y -> Timber.d(y.message()) }
                            }},
                        { ex -> Timber.d(ex) }))
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, GetBranchesInfoQuery.Node>
    ) {}

    override fun invalidate() {
        RxUtil.composedDispose(disposable)
        super.invalidate()
    }

    class DataSourceFactory(
        private val mUserName: String,
        private val mRepoName: String,
        private val mRepository: RxObservableCreator
    ): DataSource.Factory<String, GetBranchesInfoQuery.Node>()
    {
        override fun create(): DataSource<String, GetBranchesInfoQuery.Node> {
            return RepositoryBranchesInfoDataSource(mUserName, mRepoName, mRepository)
        }
    }
}