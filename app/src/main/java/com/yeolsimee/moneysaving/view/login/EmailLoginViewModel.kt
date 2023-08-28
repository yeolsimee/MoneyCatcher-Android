package com.yeolsimee.moneysaving.view.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.auth.Email
import com.yeolsimee.moneysaving.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailLoginViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    fun send(
        email: String,
        onComplete: () -> Unit = {},
        onFailure: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
        Email.send(email, onComplete = onComplete, onFailure = onFailure, onError = onError)
    }

    fun receiveEmailResult(
        intent: Intent,
        activity: Activity,
        loadingState: MutableStateFlow<Boolean>? = null,
        signedUserCallback: () -> Unit,
        newUserCallback: () -> Unit,
        onFailure: () -> Unit = {}
    ) {
        Email.receive(intent, activity, loadingState, onFailure) {
            viewModelScope.launch {
                userUseCase.login().onSuccess {
                    if (it.isNewUser == "Y") {
                        newUserCallback()
                    } else {
                        signedUserCallback()
                    }
                }.onFailure {
                    onFailure()
                }
            }
        }
    }
}