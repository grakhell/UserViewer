package ru.grakhell.oauthlib.util

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import ru.grakhell.oauthlib.manager.GitAccount
import ru.grakhell.oauthlib.service.ServiceGenerator
import ru.grakhell.oauthlib.service.TokenService

class TokenUtil {
    companion object {

        private val tokenClient:TokenService by lazy{
            ServiceGenerator
                .setCredentials(GitAccount.client_id,GitAccount.client_sec)
                .createServiceToken(TokenService::class.java)
        }

        fun checkToken(token:String):Boolean =
            runBlocking {
                async(Dispatchers.IO) {
                    var flag = false
                    tokenClient.checkToken(GitAccount.client_id,token)
                        .subscribe(
                            {it -> flag = true},
                            {it -> kotlin.run {flag = false}})
                    return@async flag
                }.await()
            }

        fun resetToken(token: String): String =
            runBlocking {
                async(Dispatchers.IO) {
                    var tn = ""
                    tokenClient.resetToken(GitAccount.client_id,token)
                        .subscribe{it -> tn = it.token}
                    return@async tn
                }.await()
            }
    }
}