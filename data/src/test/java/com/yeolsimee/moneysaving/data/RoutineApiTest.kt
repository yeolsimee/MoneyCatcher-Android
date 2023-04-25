package com.yeolsimee.moneysaving.data

import com.yeolsimee.moneysaving.data.api.RoutineApiService
import com.yeolsimee.moneysaving.data.repository.RoutineApiRepository
import com.yeolsimee.moneysaving.data.source.RoutineSource
import com.yeolsimee.moneysaving.domain.entity.ApiResponse
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class RoutineApiTest: BehaviorSpec({

    val service = mockk<RoutineApiService>()

    given("특정 날짤 루틴 조회를 하면") {
        val source = RoutineSource(service)
        val repository = RoutineApiRepository(source)
        `when`("2023년 04월 25일 때") {
            val date = "20230425"

            val expectedResult = RoutinesOfDay(
                routineDay = "20230425",
                categoryDatas = arrayOf()
            )
            coEvery {
                service.findRoutineDay(date)
            } returns Response.success(
                ApiResponse(
                    success = true,
                    code = 0,
                    data = expectedResult
                )
            )
            then("루틴 날짜는 `20230425`이고, 카테고리는 없다고 나온다.") {
                runBlocking {
                    val job = async {
                        repository.findRoutineDay(date) shouldBe Result.success(expectedResult)
                    }
                    job.await()
                }
            }
        }
    }
})