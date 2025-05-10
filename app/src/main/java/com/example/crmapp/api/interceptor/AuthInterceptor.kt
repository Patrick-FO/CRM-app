package com.example.crmapp.api.interceptor

import com.example.crmapp.data.storage.JwtStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val jwtStorage: JwtStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip token for login and registration endpoints
        val path = originalRequest.url.encodedPath
        if (path.endsWith("/auth") || path.endsWith("/user")) {
            return chain.proceed(originalRequest)
        }

        // Get token
        val token = jwtStorage.getJwtToken() ?: return chain.proceed(originalRequest)

        // Add token to request
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}