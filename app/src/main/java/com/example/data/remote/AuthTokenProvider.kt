package com.example.data.remote

/** Provides the current Supabase access token without exposing auth details to API clients. */
fun interface AuthTokenProvider {
    suspend fun accessToken(): String?
}
