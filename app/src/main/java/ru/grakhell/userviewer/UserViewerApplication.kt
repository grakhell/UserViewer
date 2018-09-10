package ru.grakhell.userviewer

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import ru.grakhell.userviewer.injection.component.DaggerAppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject



class UserViewerApplication: Application(), HasActivityInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(DebugTree())
        }

        DaggerAppComponent.builder()
                .app(this)
                .build().inject(this)

    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = androidInjector
}