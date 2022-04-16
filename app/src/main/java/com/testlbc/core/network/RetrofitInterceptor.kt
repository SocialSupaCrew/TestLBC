package com.testlbc.core.network

import okhttp3.Interceptor
import okhttp3.Response

class RetrofitInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestBuilder = request.newBuilder()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")

        request = requestBuilder.build()

        return chain.proceed(request)
    }
}
