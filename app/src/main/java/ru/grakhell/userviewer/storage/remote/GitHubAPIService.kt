package ru.grakhell.userviewer.storage.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import okhttp3.OkHttpClient
import ru.grakhell.userviewer.domain.entity.type.CustomType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GitHubAPIService private constructor(){
    companion object {
        private const val BASE_URL = "https://api.github.com/graphql"
        private var credToken: String = ""
        private val cacheFactory = LruNormalizedCacheFactory(EvictionPolicy.builder()
            .maxSizeBytes(10485760).build())
        private val apolloClient: ApolloClient by lazy {
            ApolloClient.builder()
                .serverUrl(BASE_URL)
                .normalizedCache(cacheFactory)
                .addCustomTypeAdapter(CustomType.DATETIME, DateCustomTypeAdapter())
                .okHttpClient(createOkHttpClient())
                .build()
            }

        private fun createOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .authenticator { _, response -> kotlin.run {
                    if (credToken == response.request().header("Authorization")) return@run null
                    return@run response.request().newBuilder().header("Authorization", credToken)
                        .build() } }
                .build()
        }

        fun setToken(token: String): GitHubAPIService.Companion {
            credToken = token
            return this
        }

        fun getClient(): ApolloClient {
            return apolloClient
        }

        class DateCustomTypeAdapter : CustomTypeAdapter<Date> {
            override fun decode(value: CustomTypeValue<*>): Date {
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                return format.parse(value.value.toString())
            }

            override fun encode(value: Date): CustomTypeValue<*> {
                return value as CustomTypeValue<*>
            }
        }
    }
}