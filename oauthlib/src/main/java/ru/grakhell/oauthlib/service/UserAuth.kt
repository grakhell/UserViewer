package ru.grakhell.oauthlib.service

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.grakhell.oauthlib.model.Query
import ru.grakhell.oauthlib.model.Response

interface UserAuth {

    @POST("/authorizations")
    fun authorize(
        @Body body: Query
    ): Observable<Response>

    @POST("/authorizations")
    fun authorize2FA(
        @Body body: Query,
        @Header("X-GitHub-OTP") code: String
    ): Observable<Response>
}