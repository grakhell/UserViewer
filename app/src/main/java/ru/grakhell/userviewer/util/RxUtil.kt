package ru.grakhell.userviewer.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class RxUtil {
    companion object {
        fun dispose(disposable: Disposable?){
            if (disposable!= null && !disposable.isDisposed) disposable.dispose()
        }

        fun composedDispose(compositeDisposable: CompositeDisposable?){
            if (compositeDisposable!=null&&!compositeDisposable.isDisposed){
                compositeDisposable.dispose()
            }
        }
    }
}