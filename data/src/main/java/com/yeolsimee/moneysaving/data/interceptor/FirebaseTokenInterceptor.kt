package com.yeolsimee.moneysaving.data.interceptor

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit


class FirebaseUserIdTokenInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        return try {
            val user: FirebaseUser = FirebaseAuth.getInstance().currentUser
                ?: return chain.proceed(request)
            // No has auth header
            val task: Task<GetTokenResult> = user.getIdToken(false)
            val tokenResult: GetTokenResult =
                Tasks.await(task, 10, TimeUnit.SECONDS) // Timeout 10 Seconds
            val idToken: String = tokenResult.token ?: return chain.proceed(request)
            Log.d("Auth-Header", "x-auth: $idToken")
            val newRequest = request.newBuilder()
                .addHeader(X_FIREBASE_ID_TOKEN, idToken)
                .build()
            chain.proceed(newRequest) // Has auth header
        } catch (e: Exception) {
            chain.proceed(request) // No has auth header
        }
    }

    companion object {
        private const val X_FIREBASE_ID_TOKEN = "x-auth"
    }
}