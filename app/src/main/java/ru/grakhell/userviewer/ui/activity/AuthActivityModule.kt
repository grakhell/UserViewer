package ru.grakhell.userviewer.ui.activity

import android.accounts.AccountAuthenticatorActivity
import dagger.Binds
import dagger.Module
import ru.grakhell.userviewer.injection.scope.ActivityScope
import ru.grakhell.userviewer.ui.common.BaseActivityModule

@Module(includes = [BaseActivityModule::class])
abstract class AuthActivityModule {

    @ActivityScope
    @Binds
    abstract fun activity(activity: AuthActivity):AccountAuthenticatorActivity
}