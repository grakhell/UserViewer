package ru.grakhell.userviewer.ui.activity.presenter

import android.accounts.Account
import android.content.Intent

import ru.grakhell.userviewer.ui.common.ActivityPresenter

interface ConductorPresenter : ActivityPresenter {

    fun accountPicker()

    fun invalidateAuthToken(authToken:String)

    fun getTokenForExistingAccount(
        account: Account,
        authTokenType: String
    ):Boolean

    fun isLogged():Boolean
    fun setLogged(flag:Boolean)

    fun setAccountName(name:String)
    fun getAccountName():String

    fun takeAccount(data: Intent?)

    fun clear()
}