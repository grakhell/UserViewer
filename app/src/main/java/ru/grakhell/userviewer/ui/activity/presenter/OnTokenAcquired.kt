package ru.grakhell.userviewer.ui.activity.presenter

import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import ru.grakhell.oauthlib.util.TokenUtil
import ru.grakhell.userviewer.injection.scope.ActivityScope
import ru.grakhell.userviewer.storage.Account
import ru.grakhell.userviewer.ui.activity.view.ConductorActivity
import java.security.InvalidKeyException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScope
class OnTokenAcquired @Inject constructor(private val presenterImpl: ConductorPresenterImpl) :
    AccountManagerCallback<Bundle> {


    override fun run(future: AccountManagerFuture<Bundle>?) {
        val bundle = future?.getResult(2, TimeUnit.MINUTES)
        val intent = future?.result?.get(AccountManager.KEY_INTENT) as Intent?
        if (intent != null) {
            startActivityForResult(ConductorActivity(), intent, 0,null)
        } else {
            val result = bundle?.getString(AccountManager.KEY_AUTHTOKEN).orEmpty()
            if (result.isNotEmpty()) {
                presenterImpl.checkToken(result)
            }
        }

    }
}