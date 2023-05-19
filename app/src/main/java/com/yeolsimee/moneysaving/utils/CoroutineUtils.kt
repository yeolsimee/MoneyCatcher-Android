package com.yeolsimee.moneysaving.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun executeForTimeMillis(scope: CoroutineScope, timeMillis: Long, action: suspend () -> Unit) {
    scope.launch {
        val job = scope.launch {
            action()
        }
        delay(timeMillis)
        job.cancel()
    }
}