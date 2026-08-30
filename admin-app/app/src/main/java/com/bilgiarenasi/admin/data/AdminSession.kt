package com.bilgiarenasi.admin.data

class AdminSession(
    private var token: String? = null,
) {
    fun setAccessToken(value: String?) { token = value }
    suspend fun accessToken(): String? = token
    fun clear() { token = null }
}
