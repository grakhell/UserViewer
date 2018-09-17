package ru.grakhell.userviewer.storage.source

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.apollographql.apollo.api.Response
import com.crashlytics.android.Crashlytics
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.launch
import ru.grakhell.userviewer.domain.entity.GetUserOrganisationInfoQuery
import ru.grakhell.userviewer.storage.remote.RxObservableCreator
import ru.grakhell.userviewer.util.RxUtil
import timber.log.Timber

class UserOrganisationInfoDataSource constructor(
    private val mName: String,
    private val mRepository: RxObservableCreator
)
    : PageKeyedDataSource<String, GetUserOrganisationInfoQuery.Node>() {

    private lateinit var mResponse: Response<GetUserOrganisationInfoQuery.Data>
    private lateinit var disposable: CompositeDisposable

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, GetUserOrganisationInfoQuery.Node>
    ) {
        launch {
            try {
                disposable = CompositeDisposable()
                disposable.add(mRepository.getUserOrganisationInfoObservable(mName,
                    params.requestedLoadSize, null).subscribe(
                    { x -> mResponse = checkNotNull(x)
                        if (!mResponse.hasErrors()) {
                            val items = checkNotNull(mResponse.data()
                                ?.user()
                                ?.organizations()
                                ?.edges()
                                ?.mapNotNull { y -> y.node() }
                                ?.toMutableList())
                            callback.onResult(
                                items,
                                mResponse.data()?.user()?.organizations()?.pageInfo()?.startCursor(),
                                mResponse.data()?.user()?.organizations()?.pageInfo()?.endCursor()
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
        callback: LoadCallback<String, GetUserOrganisationInfoQuery.Node>
    ) {
        launch {
            try {
                disposable.add(mRepository.getUserOrganisationInfoObservable(mName,
                    params.requestedLoadSize, params.key)
                    .subscribe(
                    { x -> mResponse = checkNotNull(x)
                        if (!mResponse.hasErrors()) {
                            val items = checkNotNull(mResponse.data()
                                ?.user()
                                ?.organizations()
                                ?.edges()
                                ?.mapNotNull { y -> y.node() }
                                ?.toMutableList())
                            callback.onResult(
                                items,
                                mResponse.data()?.user()?.organizations()?.pageInfo()?.endCursor()
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

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, GetUserOrganisationInfoQuery.Node>
    ) {}

    override fun invalidate() {
        RxUtil.composedDispose(disposable)
        super.invalidate()
    }

    class DataSourceFactory(
        private val mName: String,
        private val mRepository: RxObservableCreator
    ) : DataSource.Factory<String, GetUserOrganisationInfoQuery.Node>() {
        override fun create(): DataSource<String, GetUserOrganisationInfoQuery.Node> {
            return UserOrganisationInfoDataSource(mName, mRepository)
        }
    }
}