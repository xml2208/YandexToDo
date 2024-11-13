package com.xml.yandextodo.data.remote.api

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor

class AuthInterceptor : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        return response.request.newBuilder()
            .addHeader("Authorization", "Bearer Cerin")
            .build()
    }
}

val loggingInterceptor =
    HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }
