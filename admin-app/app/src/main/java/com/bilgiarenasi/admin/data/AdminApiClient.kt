package com.bilgiarenasi.admin.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AdminApiClient {
    fun create(baseUrl: String): AdminApi = Retrofit.Builder()
        .baseUrl(if (baseUrl.endsWith('/')) baseUrl else "$baseUrl/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(AdminApi::class.java)
}
