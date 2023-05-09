package com.yeolsimee.moneysaving.data.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

class ConnectivityInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            val request = chain.request()
            val response: Response = chain.proceed(request)
            val content = response.body()?.string() ?: ""
            Log.d("OkHttp Request", response.request().url().url().toString())
            Log.d("OkHttp Response", "code: ${response.code()}")

            val copy = request.newBuilder().build()
            val buffer = okio.Buffer()
            val body = copy.body()
            if (body != null) {
                body.writeTo(buffer)
                Log.d("OkHttp Request body", "Request Body: " + buffer.readUtf8())
            }
            Log.d("OkHttp", content)
            response.newBuilder()
                .body(ResponseBody.create(response.body()?.contentType(), content)).build()
        } catch (e: IOException) {
            e.printStackTrace()
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(404)
                .body(ResponseBody.create(null, "$e"))
                .message("서버 연결 안됨")
                .build()
        }
    }
}