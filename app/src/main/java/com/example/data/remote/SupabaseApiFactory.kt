package com.example.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object SupabaseApiFactory {
    fun questionSubmissionApi(
        baseUrl: String = "https://YOUR_PROJECT.supabase.co/functions/v1/",
    ): QuestionSubmissionApi {
        require(baseUrl.endsWith('/')) { "Supabase function base URL must end with '/'." }
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(QuestionSubmissionApi::class.java)
    }
}
