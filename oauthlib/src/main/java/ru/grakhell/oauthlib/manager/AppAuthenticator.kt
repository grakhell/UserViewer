package ru.grakhell.oauthlib.manager

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import ru.grakhell.oauthlib.R
import ru.grakhell.oauthlib.ui.AuthActivity
import timber.log.Timber

class AppAuthenticator constructor(
    private val context: Context
) : AbstractAccountAuthenticator(context) {

    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        val am = AccountManager.get(context)
        val token = am.peekAuthToken(account, authTokenType)?:""
        if (token.isEmpty()) {
            val password = am.getPassword(account)
            if (!password.isNullOrEmpty()) {
                val bundle = Bundle()
                val intent = Intent(context, AuthActivity::class.java)
                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account?.name)
                intent.putExtra(AccountManager.KEY_PASSWORD, password)
                intent.putExtra(GitAccount.ARG_IS_GET_TOKEN, true)
                intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
                bundle.putParcelable(AccountManager.KEY_INTENT, intent)
                return bundle
            }
        }
        if (token.isNotEmpty()) {
            val result = Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account?.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, token)
            return result
        }
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(GitAccount.ARG_ACCOUNT_TYPE, account?.type)
        intent.putExtra(GitAccount.ARG_AUTH_TYPE, authTokenType)
        intent.putExtra(GitAccount.ARG_IS_ADDING_NEW_ACCOUNT, true)
        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun getAuthTokenLabel(authTokenType: String?): String {
        if (GitAccount.AUTHTOKEN_TYPE_GIT_SCOPE == authTokenType) {
            return context.resources.getString(R.string.authTypeTokenScope)
        }
        return authTokenType.orEmpty()
    }

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle {
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra(GitAccount.ARG_ACCOUNT_TYPE, accountType)
        intent.putExtra(GitAccount.ARG_AUTH_TYPE, authTokenType)
        intent.putExtra(GitAccount.ARG_IS_ADDING_NEW_ACCOUNT, true)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        options: Bundle?
    ): Bundle? = null

    override fun updateCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle? = null

    override fun hasFeatures(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        features: Array<out String>?
    ): Bundle? = null

    override fun editProperties(p0: AccountAuthenticatorResponse?, p1: String?): Bundle? = null
}