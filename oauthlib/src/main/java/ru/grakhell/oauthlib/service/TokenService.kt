package ru.grakhell.oauthlib.service

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import io.reactivex.Observable
import ru.grakhell.oauthlib.model.TokenServiceResponse

interface TokenService {

    @GET("/applications/{client_id}/tokens/{access_token}")
    fun checkToken(
        @Path("client_id")clientId: String,
        @Path("access_token")token: String
    ): Observable<TokenServiceResponse>

    @POST("/applications/{client_id}/tokens/{access_token}")
    fun resetToken(
        @Path("client_id")clientId: String,
        @Path("access_token")token: String
    ): Observable<TokenServiceResponse>
}