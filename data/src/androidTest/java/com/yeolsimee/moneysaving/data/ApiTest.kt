package com.yeolsimee.moneysaving.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.yeolsimee.moneysaving.data.api.TestApiService
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApiTest {

    lateinit var testApiService: TestApiService

    @Before
    fun init() {
        val netModule = NetModule()

        val gson = netModule.provideGson()
        val interceptor = netModule.interceptor()
        val client = netModule.provideOkhttpClient(interceptor)

        val retrofit = netModule.provideRetrofit(gson, client)

        testApiService = retrofit.create(TestApiService::class.java)
    }

    @Test
    fun http_통신_테스트() {
        runBlocking {
            val job = async {
                val apiResult = testApiService.getTestValues(11).body() as LinkedTreeMap<*, *>?
                if (apiResult != null) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    println(gson.toJson(apiResult))
                }
                assertNotNull(apiResult)
            }
            job.await()
        }
    }
}