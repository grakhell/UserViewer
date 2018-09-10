package ru.grakhell.userviewer.storage.source

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.apollographql.apollo.api.Response
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.launch
import ru.grakhell.userviewer.domain.entity.GetStargazersInfoQuery
import ru.grakhell.userviewer.storage.remote.RxObservableCreator
import ru.grakhell.userviewer.util.RxUtil
import timber.log.Timber

class RepositoryStargazersInfoDateSource constructor(
    private val mUserName:String,
    private val mRepoName:String,
    private var mRepository: RxObservableCreator)
    : PageKeyedDataSource<String, GetStargazersInfoQuery.Node>() {


    private lateinit var mResponse: Response<GetStargazersInfoQuery.Data>
    private val disposable:CompositeDisposable = CompositeDisposable()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, GetStargazersInfoQuery.Node>
    ) {
        launch {
            try {
               disposable.add(mRepository.getStargazesInfoObservable(mUserName, params
                .requestedLoadSize, null,mRepoName)
                    .subscribe(
                        { x -> mResponse = checkNotNull(x)
                            if (!mResponse.hasErrors()) {
                                val items = checkNotNull(mResponse.data()
                                    ?.repository()
                                    ?.stargazers()
                                    ?.edges()
                                    ?.mapNotNull { y -> y.node() }
                                    ?.toMutableList())
                                callback.onResult(
                                    items,
                                    mResponse.data()?.repository()?.stargazers()?.pageInfo()?.startCursor(),
                                    mResponse.data()?.repository()?.stargazers()?.pageInfo()?.endCursor()
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
        callback: LoadCallback<String, GetStargazersInfoQuery.Node>
    ) {
        launch {
            try {
               disposable.add(mRepository.getStargazesInfoObservable(mUserName, params
                   .requestedLoadSize,
                params.key,mRepoName)
                    .subscribe(
                        { x -> mResponse = checkNotNull(x)
                            if (!mResponse.hasErrors()) {
                                val items = checkNotNull(mResponse.data()
                                    ?.repository()
                                    ?.stargazers()
                                    ?.edges()
                                    ?.mapNotNull { y -> y.node() }
                                    ?.toMutableList())
                                callback.onResult(
                                    items,
                                    mResponse.data()?.repository()?.stargazers()?.pageInfo()?.endCursor())
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
        callback: LoadCallback<String, GetStargazersInfoQuery.Node>
    ) {}

    override fun invalidate() {
        RxUtil.composedDispose(disposable)
        super.invalidate()
    }

    class DataSourceFactory(
        private val mUserName: String,
        private val mRepoName: String,
        private var mRepository: RxObservableCreator
    ): DataSource.Factory<String, GetStargazersInfoQuery.Node>()
    {
        override fun create(): DataSource<String, GetStargazersInfoQuery.Node> {
            return RepositoryStargazersInfoDateSource(mUserName, mRepoName, mRepository)
        }
    }
}