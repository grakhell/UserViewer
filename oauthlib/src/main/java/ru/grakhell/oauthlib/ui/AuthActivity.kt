package ru.grakhell.oauthlib.ui

import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_auth.view.*
import kotlinx.coroutines.experimental.runBlocking
import ru.grakhell.oauthlib.R
import ru.grakhell.oauthlib.manager.GitAccount

class AuthActivity : FragmentActivity() {

    private lateinit var viewModel: AuthViewModel

    private var mAccountAuthenticatorResponse: AccountAuthenticatorResponse? = null
    private var mResultBundle: Bundle? = null
    private var mAccountManager: AccountManager? = null

    private fun setAccountAuthenticatorResult(result: Bundle) {
        mResultBundle = result
    }

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

        setContentView(R.layout.activity_auth)

        mAccountAuthenticatorResponse = intent.getParcelableExtra(
            AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE
        )
        mAccountManager = AccountManager.get(baseContext)

        viewModel.isAddingNewAccount.value = intent.getBooleanExtra(
            GitAccount.ARG_IS_ADDING_NEW_ACCOUNT, false)

        viewModel.isGetToken.value = intent.getBooleanExtra(
            GitAccount.ARG_IS_GET_TOKEN, false)

        if (viewModel.isAddingNewAccount.value == true) {
            viewModel.accountType.value = intent.getStringExtra(GitAccount.ARG_ACCOUNT_TYPE)
            viewModel.authTokenType.value = intent.getStringExtra(GitAccount.ARG_AUTH_TYPE)
        }

        if (viewModel.isGetToken.value == true) {
            viewModel.userName.value = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            viewModel.userKey.value = intent.getStringExtra(AccountManager.KEY_PASSWORD)
        }

        viewModel.response.observe(
            this,
            Observer { _ -> run { responseSubscriber() } })

        viewModel.is2FactorAuth.observe(
            this,
            Observer { item -> kotlin.run { authSubscriber(item) } }
        )

        viewModel.errCodes.observe(
            this,
            Observer { item -> kotlin.run { codesSubscriber(item) } }
        )

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse?.onRequestContinued()
        }
    }

    override fun onStart() {
        if (viewModel.isAddingNewAccount.value == true) {
            val fr = CredentialsFragment.newInstance()
            replaceFragment(R.id.fragmentView, fr)
        }
        if (viewModel.isGetToken.value == true) {
            runBlocking { viewModel.getResponse().await() }
        }
        super.onStart()
    }

    private fun replaceFragment(@IdRes containerViewId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(containerViewId, fragment)
            .commit()
    }

    private fun responseSubscriber() {

        val bundle = Bundle()
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, viewModel.userName.value)
        bundle.putString(AccountManager.KEY_PASSWORD, viewModel.userKey.value)
        bundle.putString(AccountManager.KEY_AUTHTOKEN, viewModel.response.value?.token)
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, GitAccount.ACCOUNT_TYPE)

        val acc = Account(viewModel.userName.value, GitAccount.ACCOUNT_TYPE)

        mAccountManager?.addAccountExplicitly(acc, viewModel.userKey.value, null)
        mAccountManager?.setAuthToken(acc, GitAccount.AUTHTOKEN_TYPE_GIT_SCOPE, viewModel.response.value?.token)

        // val preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)

        viewModel.is2FactorAuth.value = false

        setAccountAuthenticatorResult(bundle)
        finish()
    }

    private fun authSubscriber(item: Boolean) {
        if (item) {
            val fr = SecondFactorFragment.newInstance()
            replaceFragment(R.id.fragmentView, fr)
        }
    }

    private fun codesSubscriber(item: Int) {
        when (item) {
            AuthViewModel.BAD_CREDS -> {
                Snackbar.make(
                    findViewById(R.id.fragmentView),
                    resources.getString(R.string.error_bad_creds),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(resources.getString(R.string.action_dismiss)) {}
                    .setActionTextColor(Color.WHITE)
                    .show()
            }
            AuthViewModel.BAD_CODE -> {
                Snackbar.make(
                    findViewById(R.id.fragmentView),
                    resources.getString(R.string.error_bad_code),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(resources.getString(R.string.action_dismiss)) {}
                    .setActionTextColor(Color.WHITE)
                    .show()
            }
            AuthViewModel.NET_ERROR -> {
                Snackbar.make(
                    findViewById(R.id.fragmentView),
                    resources.getString(R.string.error_net),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(resources.getString(R.string.action_dismiss)) {}
                    .setActionTextColor(Color.WHITE)
                    .show()
            }
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