package com.example.extraaedge.network

import android.util.Log
import com.example.extraaedge.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import javax.inject.Inject

class ApiInterceptor @Inject internal constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        // build new request
        val builder = request.newBuilder()
        request = builder.header("Authorization", "")
            .method(request.method, request.body)
            .addHeader("content-type", "application/json; charset=utf-8")
            .build()
        val response = chain.proceed(request)
        if (response.body != null && BuildConfig.DEBUG) {
            val responseBody = getResponseString(response)
            Log.e("Response::%s ", responseBody.toString())
        }
        return response
    }

    /**
     * Returns a copy of String `response.body()` without consuming it.
     */
    private fun getResponseString(response: Response): String? {
        try {
            val responseBody = response.body
            val source = Objects.requireNonNull(responseBody)!!.source()
            source.request(Long.MAX_VALUE) // request the entire body.
            val buffer = source.buffer()
            // clone buffer before reading from it
            return buffer.clone().readString(Charset.forName("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}
