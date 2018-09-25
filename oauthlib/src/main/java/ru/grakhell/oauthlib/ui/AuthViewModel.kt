package ru.grakhell.oauthlib.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.coroutineScope
import retrofit2.HttpException
import ru.grakhell.oauthlib.manager.GitAccount
import ru.grakhell.oauthlib.model.Query
import ru.grakhell.oauthlib.model.Response
import ru.grakhell.oauthlib.service.ServiceGenerator
import ru.grakhell.oauthlib.service.UserAuth
import ru.grakhell.oauthlib.util.RxUtil
import timber.log.Timber

class AuthViewModel(private val context: Application) : AndroidViewModel(context) {

    var isAddingNewAccount: MutableLiveData<Boolean> = MutableLiveData()
    var isGetToken: MutableLiveData<Boolean> = MutableLiveData()

    var is2FactorAuth: MutableLiveData<Boolean> = MutableLiveData()

    var response: MutableLiveData<Response> = MutableLiveData()
    var errCodes: MutableLiveData<Int> = MutableLiveData()

    var userName: MutableLiveData<String> = MutableLiveData()
    var userKey: MutableLiveData<String> = MutableLiveData()
    var userToken: MutableLiveData<String> = MutableLiveData()

    var accountType: MutableLiveData<String> = MutableLiveData()
    var authTokenType: MutableLiveData<String> = MutableLiveData()

    private lateinit var client:UserAuth
    private val disposable = CompositeDisposable()

    private fun initClient() {
        client = ServiceGenerator
            .setCredentials(userName.value.orEmpty(), userKey.value.orEmpty())
            .createService(UserAuth::class.java)
    }

    suspend fun getResponse() = coroutineScope {
        initClient()
        async(Dispatchers.IO) {
            disposable.add(
                client.authorize(
                    Query(
                        GitAccount.client_id,
                        GitAccount.client_sec,
                        GitAccount.SCOPE,
                        GitAccount.key_note))
                    .subscribe(
                        { item -> response.postValue(item) },
                        { ex -> kotlin.run {
                            if (ex is HttpException) {
                                parseErrorResponse(ex)
                            } else {
                                Timber.e(ex)
                            }}}))
        }
    }

    suspend fun getResponse2FA() =
        coroutineScope {
            async(Dispatchers.IO) {
                disposable.add(
                    client.authorize2FA(
                        Query(
                            GitAccount.client_id,
                            GitAccount.client_sec,
                            GitAccount.SCOPE,
                            GitAccount.key_note),
                        userToken.value.orEmpty())
                        .subscribe(
                            { item -> response.postValue(item) },
                            { ex -> kotlin.run {
                                if (ex is HttpException) {
                                    parseErrorResponse(ex)
                                } else {
                                    Timber.e(ex)
                                }}}))
                }
        }

    private fun parseErrorResponse(ex: HttpException) {
        Timber.d("${ex.code()}: ${ex.response().message()}")
        Timber.d(ex.response().errorBody()?.string())
        when(ex.code()) {
            401 -> {
                val head = ex.response().headers().get(HEADER_2FA)
                if (!head.isNullOrEmpty()) {
                    if (is2FactorAuth.value == true) {
                        errCodes.postValue(BAD_CODE)
                    } else {
                        is2FactorAuth.postValue(true)
                    }
                } else {
                    errCodes.postValue(BAD_CREDS)
                }
            }
        }
    }

    override fun onCleared() {
        RxUtil.composedDispose(disposable)
        super.onCleared()
    }

    companion object {
        const val BAD_CREDS = 1
        const val BAD_CODE = 2
        const val NET_ERROR = 3
        const val HEADER_2FA = "X-GitHub-OTP"
    }
}
