package ru.grakhell.userviewer.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import okhttp3.OkHttpClient
import ru.grakhell.userviewer.R
import ru.grakhell.userviewer.ui.activity.AuthActivity
import javax.inject.Inject

class AppAuthenticator constructor(
    val context: Context
):AbstractAccountAuthenticator(context) {

    private val BASE_URL:String = "https://api.github.com"
    private val client = OkHttpClient.Builder()
        .build()

    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        val am = AccountManager.get(context)
        val token = am.peekAuthToken(account, authTokenType)
        if (token.isEmpty()) {
            val password = am.getPassword(account)
            if (!password.isNullOrEmpty()) {
                TODO("server auth code")
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
        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT,intent)
        return bundle
    }

    override fun getAuthTokenLabel(authTokenType: String?): String {
        if(GitAccount.AUTHTOKEN_TYPE_GIT_SCOPE == authTokenType) {
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
        val intent = Intent(context,AuthActivity::class.java)
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