package ru.grakhell.oauthlib.ui

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import ru.grakhell.oauthlib.auth.GitAccount

class AuthActivity : FragmentActivity() {

    private lateinit var viewModel: AuthViewModel

    private var mAccountAuthenticatorResponse: AccountAuthenticatorResponse? = null
    private var mResultBundle: Bundle? = null

    fun setAccountAuthenticatorResult(result: Bundle) {
        mResultBundle = result
    }

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

        mAccountAuthenticatorResponse = intent.getParcelableExtra(
            AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE
        )

        viewModel.isAddingNewAccount = intent
            .getBooleanExtra(GitAccount.ARG_IS_ADDING_NEW_ACCOUNT, false)
        viewModel.isGetToken = intent
            .getBooleanExtra(GitAccount.ARG_IS_GET_TOKEN, false)

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse?.onRequestContinued()
        }
    }

    override fun finish() {
        if (mAccountAuthenticatorResponse != null) {
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse?.onResult(mResultBundle)
            } else {
                mAccountAuthenticatorResponse?.onError(
                    AccountManager.ERROR_CODE_CANCELED,
                    "canceled"
                )
            }
            mAccountAuthenticatorResponse = null
        }
        super.finish()
    }

}