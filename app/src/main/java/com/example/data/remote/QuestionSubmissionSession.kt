package com.example.data.remote

/**
 * Holds the current Supabase access token in memory for public player requests.
 *
 * The authentication feature must call [updateAccessToken] after a successful
 * Supabase sign-in and [clear] when the user signs out.  No token is persisted
 * here, so a stale credential cannot survive an app restart.
 */
object QuestionSubmissionSession {
    @Volatile
    private var token: String? = null

    fun updateAccessToken(accessToken: String) {
        token = accessToken.trim().takeIf { it.isNotEmpty() }
    }

    fun clear() {
        token = null
    }

    fun accessToken(): String? = token
}
