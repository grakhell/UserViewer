package ru.grakhell.userviewer.util

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RxEventBus @Inject constructor() {
    private val mBackpressureStrategy = BackpressureStrategy.BUFFER
    private val mBusSubject: PublishSubject<Any> = PublishSubject.create()

    fun post(event: Any) {
        mBusSubject.onNext(event)
    }

    fun toFlowable(): Flowable<Any> {
        return mBusSubject.toFlowable(mBackpressureStrategy)
    }

    fun <T> toFilteredFlowable(eventClass: Class<T>): Flowable<T> {
        return mBusSubject.ofType(eventClass).toFlowable(mBackpressureStrategy)
    }
}