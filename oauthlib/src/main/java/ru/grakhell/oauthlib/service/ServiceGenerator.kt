package ru.grakhell.oauthlib.service

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.reflect.KClass

class ServiceGenerator {

    companion object {
        const val BASE_URL: String = "https://api.github.com"
        private val httpClient = OkHttpClient.Builder()
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        private lateinit var authToken:String

        fun <T> createService(
            @NotNull serviceClass: Class<T>
        ):T {
            if (authToken.isNotEmpty()) {
                httpClient.authenticator {_, response -> run{
                    if(authToken == response.request().header("Authorization")) return@run null
                    return@run response.request().newBuilder().header("Authorization", authToken)
                        .build()
                }}
                retrofit.client(httpClient.build())
            }
            return retrofit.build().create(serviceClass)
        }

        fun setCredentials(
            @NotNull userName:String,
            @NotNull userPass:String
        ):ServiceGenerator.Companion {
            authToken = Credentials.basic(userName,userPass)
            return this
        }

        fun setAuthToken(
            @NotNull token:String
        ):ServiceGenerator.Companion {
            authToken = token
            return this
        }
    }

}