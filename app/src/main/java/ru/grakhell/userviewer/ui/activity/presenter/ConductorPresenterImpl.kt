package ru.grakhell.userviewer.ui.activity.presenter

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import ru.grakhell.oauthlib.manager.GitAccount
import ru.grakhell.oauthlib.util.TokenUtil
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.injection.scope.ActivityScope
import ru.grakhell.userviewer.ui.activity.view.ACCOUNT_CODE
import ru.grakhell.userviewer.ui.activity.view.Conductor
import ru.grakhell.userviewer.ui.common.BaseActivityPresenter
import timber.log.Timber
import java.lang.Exception
import java.security.InvalidKeyException
import javax.inject.Inject

@ActivityScope
class ConductorPresenterImpl @Inject constructor(
    activity: Conductor?,
    var acc:ru.grakhell.userviewer.storage.Account
) : BaseActivityPresenter<Conductor>(activity), ConductorPresenter {

    private var _isLogged:Boolean = false
    private val mAccountManager: AccountManager by lazy {AccountManager.get(mActivity as Activity) }

    override fun accountPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = AccountManager.newChooseAccountIntent(
                null,
                null,
                arrayOf(GitAccount.ACCOUNT_TYPE),
                null,
                null,
                null,
                null)
            startActivityForResult(
                mActivity as Activity,
                intent,
                ACCOUNT_CODE,
                null
            )
        } else {
            val intent = AccountManager.newChooseAccountIntent(
                null,
                null,
                arrayOf(GitAccount.ACCOUNT_TYPE),
                false,
                null,
                null,
                null,
                null)
            startActivityForResult(
                mActivity as Activity,
                intent,
                ACCOUNT_CODE,
                null
            )
        }
    }

    override fun invalidateAuthToken(
        authToken: String
    ) {
        runBlocking {
            try {
                mAccountManager
                    .invalidateAuthToken(GitAccount.ACCOUNT_TYPE, authToken)
            } catch ( ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    override fun getTokenForExistingAccount(
        account: Account,
        authTokenType: String
    ):Boolean {
        return runBlocking{
            try {
                async(Dispatchers.Default) {
                    val future =
                        mAccountManager
                            .getAuthToken(
                        account,
                        authTokenType,
                        null,
                        mActivity as Activity,
                        OnTokenAcquired(this@ConductorPresenterImpl),
                        null
                    )
                }.await()
                return@runBlocking true
            } catch (ex:InvalidKeyException) {

                Timber.e(ex)
                return@runBlocking false
            } catch (ex: Exception) {
                Timber.e(ex)
                return@runBlocking false
            }
        }
    }

    override fun isLogged(): Boolean  = _isLogged

    override fun setLogged(flag:Boolean) {
        _isLogged = flag
    }

    override fun takeAccount(data: Intent?) {
        val accName = data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        val accType = data?.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)
        val accs = mAccountManager.getAccountsByType(accType)
        try {
            accs.forEach { x -> run {
                if(x.name == accName) { acc.setAccount(x.name)
                getTokenForExistingAccount(x,GitAccount.AUTHTOKEN_TYPE_GIT_SCOPE)
            }}}
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    fun checkToken(token:String) {
            if(TokenUtil.checkToken(token)) {
                acc.setKey(token)
                Snackbar.make(
                    checkNotNull(mActivity?.getView()),
                    R.string.actionLogInSuccess,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.ok_string) {}
                    .setActionTextColor(Color.WHITE)
                    .show()
                _isLogged = true
                mActivity?.showMenuItems(_isLogged)
            } else {
                Snackbar.make(
                    checkNotNull(mActivity?.getView()),
                    R.string.actionLogInError,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.ok_string) {}
                    .setActionTextColor(Color.WHITE)
                    .show()
                _isLogged = false
                mActivity?.showMenuItems(_isLogged)
            }
    }

    override fun onStart(savedInstanceState: Bundle?) {
        _isLogged = savedInstanceState?.getBoolean("logged")?:false
        super.onStart(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean("logged",_isLogged)
        super.onSaveInstanceState(outState)
    }

    override fun getAccountName(): String = acc.getAccount()

    override fun setAccountName(name: String) {
        acc.setAccount(name)
    }

    override fun clear() {
        acc.clearAccountInfo()
    }
}