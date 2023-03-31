package com.yeolsimee.moneysaving.view.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.domain.usecase.SampleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(private val sampleUseCase: SampleUseCase) :
    ContainerHost<SampleState, SampleSideEffect>, ViewModel() {

    override val container = container<SampleState, SampleSideEffect>(SampleState())
    private var testJob: Job? = null


    init {
        getData(12)
    }

    fun getData(round: Int) = intent {
        testJob?.cancel()
        testJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = sampleUseCase(round)

                withContext(Dispatchers.Main) {

                    if (result.isNullOrBlank()) {
                        showSideEffect("result is null")
                    } else {
                        reduce {
                            generateSampleStateFromSampleData(result)
                        }
                    }
                }
            } catch (e: RuntimeException) {
                showSideEffect(e.message)
            }
        }
    }

    private fun showSideEffect(message: String?) {
        intent {
            postSideEffect(SampleSideEffect.Toast(message ?: "Unknown Error Message"))
        }
    }

}