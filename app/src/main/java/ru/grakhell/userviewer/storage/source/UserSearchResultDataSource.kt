package ru.grakhell.userviewer.storage.source

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.apollographql.apollo.api.Response
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.launch
import ru.grakhell.userviewer.domain.entity.GetUserSearchResultQuery
import ru.grakhell.userviewer.storage.remote.RxObservableCreator
import ru.grakhell.userviewer.util.RxUtil
import timber.log.Timber

class UserSearchResultDataSource constructor(
       private val mName: String,
       private val mRepository: RxObservableCreator)
    :PageKeyedDataSource<String,GetUserSearchResultQuery.AsUser>() {

    private lateinit var mResponse:Response<GetUserSearchResultQuery.Data>
    private var disposable: CompositeDisposable = CompositeDisposable()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, GetUserSearchResultQuery.AsUser>
    ) {
        launch {
            try {
                disposable.add(mRepository.getUserSearchResultObservable(mName, params.requestedLoadSize, null)
                    .subscribe(
                    { x ->
                        run{mResponse = checkNotNull(x)
                        if (!mResponse.hasErrors()) {
                            val items = checkNotNull(mResponse.data()
                                ?.search()
                                ?.edges()
                                ?.map { x -> x.node() }
                                ?.map { x -> x  as GetUserSearchResultQuery.AsUser }
                                ?.toMutableList())
                            callback.onResult(
                                items,
                                mResponse.data()?.search()?.pageInfo()?.startCursor(),
                                mResponse.data()?.search()?.pageInfo()?.endCursor()
                            )
                            } else {
                            mResponse.errors().forEach { x -> Timber.e(x.message()) }
                                }
                        }
                    },
                    { ex -> Timber.e(ex) }))
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, GetUserSearchResultQuery.AsUser>
    ) {
        launch {
            try {
               disposable.add(mRepository.getUserSearchResultObservable(
                   mName,
                   params.requestedLoadSize,
                   params.key).subscribe(
                    { x ->
                        run{mResponse = checkNotNull(x)
                            if (!mResponse.hasErrors()) {
                                val items = checkNotNull(mResponse.data()
                                    ?.search()
                                    ?.edges()
                                    ?.map { x -> x.node() }
                                    ?.map { x -> x  as GetUserSearchResultQuery.AsUser}
                                    ?.toMutableList())
                                callback.onResult(
                                    items,
                                    mResponse.data()?.search()?.pageInfo()?.endCursor()
                                )
                            } else {
                                mResponse.errors().forEach { x -> Timber.e(x.message()) }
                            }
                        }
                    },
                    { ex -> Timber.e(ex) }))
            } catch (e: Exception) {
                Timber.e(e)
            }

        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, GetUserSearchResultQuery.AsUser>
    ) {}

    override fun invalidate() {
        RxUtil.composedDispose(disposable)
        super.invalidate()
    }

        class DataSourceFactory(
            private val mName: String,
            private val mRepository: RxObservableCreator
    ):DataSource.Factory<String,GetUserSearchResultQuery.AsUser>()
    {
        override fun create(): DataSource<String, GetUserSearchResultQuery.AsUser> {
            return UserSearchResultDataSource(mName, mRepository)
        }
    }

}