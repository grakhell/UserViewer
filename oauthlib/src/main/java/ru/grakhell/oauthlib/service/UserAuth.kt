package ru.grakhell.oauthlib.service

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.grakhell.oauthlib.manager.GitAccount
import ru.grakhell.oauthlib.model.Query
import ru.grakhell.oauthlib.model.Response

interface UserAuth {

    /*@PUT("/authorizations/clients/${GitAccount.client_id}")
    fun authorize(
        clientSec: String,
        scopes:String
    ): Observable<Response>*/

    @POST("/authorizations")
    fun authorize(
        @Body body:Query
    ): Observable<Response>

    @POST("/authorizations")
    fun authorize2FA(
        @Body body:Query,
        @Header("X-GitHub-OTP") code:String
    ):Observable<Response>
}