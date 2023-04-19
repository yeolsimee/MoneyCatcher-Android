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
            val response: Response = chain.proceed(chain.request())
            val content = response.body()?.string() ?: ""
            Log.d("OkHttp Request", response.request().url().url().toString())
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