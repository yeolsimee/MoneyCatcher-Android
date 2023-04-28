package com.yeolsimee.moneysaving.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DateUtilsTest : BehaviorSpec({
    given("24시간 기준으로 표현된 hour를 오전/오후로 나누어서 12시간 기준으로 hour를 문자열로 표현하면") {
        `when`("23시를 변환할 때") {
            val hour = 23
            val result = hour.getTwoDigitsHour()
            then("`오후 11`여야 한다.") {
                result shouldBe "오후 11"
            }
        }
        `when`("3시를 변환할 때") {
            val hour = 3
            val result = hour.getTwoDigitsHour()
            then("`오전 03`여야 한다.") {
                result shouldBe "오전 03"
            }
        }
        `when`("-33시를 변환할 때") {
            then("범위 예외 상황을 반환한다.") {
                val exception = shouldThrow<NumberFormatException> {
                    val hour = -33
                    hour.getTwoDigitsHour()
                }
                exception.message shouldBe "시간 표현 범위 초과"
            }
        }
        `when`("25시를 변환할 때") {
            then("범위 예외 상황을 반환한다.") {
                val exception = shouldThrow<NumberFormatException> {
                    val hour = 25
                    hour.getTwoDigitsHour()
                }
                exception.message shouldBe "시간 표현 범위 초과"
            }
        }
    }
})