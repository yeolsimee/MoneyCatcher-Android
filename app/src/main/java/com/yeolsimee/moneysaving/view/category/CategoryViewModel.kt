package com.yeolsimee.moneysaving.view.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import com.yeolsimee.moneysaving.domain.repository.ICategoryApiRepository
import com.yeolsimee.moneysaving.view.ISideEffect
import com.yeolsimee.moneysaving.view.ToastSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryApi: ICategoryApiRepository) :
    ContainerHost<MutableList<TextItem>, ToastSideEffect>, ISideEffect, ViewModel() {

    override val container = container<MutableList<TextItem>, ToastSideEffect>(mutableListOf())

    init {
        getCategoryList()
    }

    private fun getCategoryList() = intent {
        viewModelScope.launch {
            val result = categoryApi.getCategoryList()
            result.onSuccess {
                reduce { it }
            }.onFailure {
                showSideEffect(it.message)
            }
        }
    }

    override fun showSideEffect(message: String?) {
        intent {
            reduce { mutableListOf() }
            postSideEffect(ToastSideEffect.Toast(message ?: "Unknown Error Message"))
        }
    }

    fun addCategory(name: String) = intent {
        viewModelScope.launch {
            val result = categoryApi.addCategory(name)
            result.onSuccess { addedItem ->
                val addedList = mutableListOf<TextItem>()
                state.forEach { addedList.add(it) }
                addedList.add(addedItem)
                reduce { addedList }
            }.onFailure {
                showSideEffect(it.message)
            }
        }
    }
}