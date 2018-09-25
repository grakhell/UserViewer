package ru.grakhell.oauthlib.util

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.coroutineScope
import ru.grakhell.oauthlib.manager.GitAccount
import ru.grakhell.oauthlib.service.ServiceGenerator
import ru.grakhell.oauthlib.service.TokenService

class TokenUtil {
    companion object {

        private val client:TokenService by lazy{
            ServiceGenerator
                .setCredentials(GitAccount.client_id,GitAccount.client_sec)
                .createService(TokenService::class.java)
        }

        suspend fun checkToken(token:String):Boolean =
            coroutineScope {
                async(Dispatchers.IO) {
                    var flag = false
                    client.checkToken(GitAccount.client_id,token)
                        .subscribe(
                            {flag = true},
                            {flag = false})
                    return@async flag
                }.await()
            }

        suspend fun resetToken(token: String): String =
            coroutineScope {
                async(Dispatchers.IO) {
                    var tn = ""
                    client.resetToken(GitAccount.client_id,token)
                        .subscribe{it -> tn = it.token}
                    return@async tn
                }.await()
            }
    }
}