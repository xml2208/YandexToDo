package com.xml.yandextodo.data.remote.api

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor

const val AUTH_TOKEN_VALUE = "Bearer Cerin"

class AuthInterceptor : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        return response.request.newBuilder()
            .addHeader("Authorization", AUTH_TOKEN_VALUE)
            .build()
    }
}

val loggingInterceptor =
    HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }
