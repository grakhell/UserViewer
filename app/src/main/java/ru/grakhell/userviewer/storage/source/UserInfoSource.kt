package ru.grakhell.userviewer.storage.source

import com.apollographql.apollo.api.Response
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import ru.grakhell.userviewer.domain.entity.GetUserInfoQuery
import ru.grakhell.userviewer.storage.remote.RxObservableCreator
import timber.log.Timber

class UserInfoSource(
    private val name:String,
    private val mRepository: RxObservableCreator
) {

    private lateinit var mResponse: Response<GetUserInfoQuery.Data>
    private val publisher: BehaviorSubject<GetUserInfoQuery.User?> = BehaviorSubject.create()


    fun parse(): Observable<GetUserInfoQuery.User?> {
        getInfo()
        return publisher
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getInfo() {
        val disposable = mRepository.getUserInfoObservable(name)
            .subscribe(
                { x ->
                    mResponse = x
                    if (!mResponse.hasErrors()) {
                        publisher.onNext(checkNotNull(mResponse.data()?.user()))
                    } else {
                        mResponse.errors().forEach { y ->
                            Timber.d(y.message())
                            publisher.onError(Exception(y.message()))
                        }
                    }},
                { ex -> Timber.d(ex)
                publisher.onError(ex)})
    }
}