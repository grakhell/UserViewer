package ru.grakhell.userviewer.storage.source

import com.apollographql.apollo.api.Response
import com.crashlytics.android.Crashlytics
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import ru.grakhell.userviewer.domain.entity.GetRepositoryInfoQuery
import ru.grakhell.userviewer.storage.remote.RxObservableCreator
import ru.grakhell.userviewer.util.RxUtil
import timber.log.Timber

class RepositoryInfoSource(
    private val name: String,
    private val repositoryName: String,
    private val mRepository: RxObservableCreator
) {

    private lateinit var mResponse: Response<GetRepositoryInfoQuery.Data>
    private val publisher: BehaviorSubject<GetRepositoryInfoQuery.Repository?> = BehaviorSubject.create()
    private var disposable: Disposable? = null

    fun parse(): Observable<GetRepositoryInfoQuery.Repository?> {
        getInfo()
        return publisher
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
        }

    private fun getInfo() {
        disposable = mRepository.getRepositoryInfoObservable(name, repositoryName)
            .subscribe(
                { x ->
                    mResponse = x
                    if (!mResponse.hasErrors()) {
                        publisher.onNext(checkNotNull(mResponse.data()?.repository()))
                    } else {
                        mResponse.errors().forEach { y ->
                            Timber.d(y.message())
                            Crashlytics.log(y.message())
                            y.customAttributes().forEach { z -> Crashlytics.log(z.toString()) }
                            publisher.onError(Exception(y.message()))
                        }
                    } },
                { ex ->
                    Timber.d(ex)
                    Crashlytics.logException(ex)
                    publisher.onError(ex) },
                { publisher.onComplete()
                RxUtil.dispose(disposable) })
    }
}
