package com.yeolsimee.moneysaving.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgreementViewModel @Inject constructor(private val useCase: UserUseCase) : ViewModel() {

    fun signUp(onSuccess: () -> Unit, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            useCase.signUp().onSuccess {
                onSuccess()
            }.onFailure {
                onFailure()
            }
        }
    }

}