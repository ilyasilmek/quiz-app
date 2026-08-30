package com.bilgiarenasi.admin.data

class AdminRepository(
    private val api: AdminApi,
    private val accessTokenProvider: suspend () -> String?,
) {
    suspend fun loadQueue(): Result<AdminQueueResponse> = runCatching {
        val token = accessTokenProvider() ?: error("Admin authentication required")
        // Retrofit endpoint uses explicit authorization to keep the private boundary obvious.
        api.queue(authorization = "Bearer $token")
    }

    suspend fun review(request: AdminReviewRequest): Result<Map<String, Any?>> = runCatching {
        val token = accessTokenProvider() ?: error("Admin authentication required")
        api.review(request, authorization = "Bearer $token")
    }
}
