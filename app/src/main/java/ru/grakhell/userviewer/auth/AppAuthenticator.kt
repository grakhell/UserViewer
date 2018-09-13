package ru.grakhell.userviewer.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import okhttp3.OkHttpClient
import ru.grakhell.userviewer.ui.activity.AuthActivity
import javax.inject.Inject

class AppAuthenticator @Inject constructor(
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAuthTokenLabel(authTokenType: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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