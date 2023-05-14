package com.yeolsimee.moneysaving.view.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import com.yeolsimee.moneysaving.domain.repository.ICategoryApiRepository
import com.yeolsimee.moneysaving.ui.side_effect.ApiCallSideEffect
import com.yeolsimee.moneysaving.ui.side_effect.IApiCallSideEffect
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
    ContainerHost<MutableList<TextItem>, ApiCallSideEffect>, IApiCallSideEffect, ViewModel() {

    override val container = container<MutableList<TextItem>, ApiCallSideEffect>(mutableListOf())

    init {
        getCategoryList()
    }

    private fun getCategoryList() = intent {
        showLoading()
        reduce { mutableListOf() }
        viewModelScope.launch {
            val result = categoryApi.getCategoryList()
            result.onSuccess {
                reduce { it }
            }.onFailure {
                showEmpty()
            }
        }
    }

    override fun showLoading() {
        intent {
            postSideEffect(ApiCallSideEffect.Loading)
        }
    }

    override fun showEmpty() {
        intent {
            reduce { mutableListOf() }
            postSideEffect(ApiCallSideEffect.Empty)
        }
    }

    fun addCategory(name: String) = intent {
        viewModelScope.launch {
            val result = categoryApi.addCategory(name)
            result.onSuccess { addedItem ->
                val addedList = state.map { it }.toMutableList()
                addedList.add(addedItem)
                reduce { addedList }
            }.onFailure {
                showEmpty()
            }
        }
    }

    fun delete(category: TextItem) = intent {
        viewModelScope.launch {
            categoryApi.delete(category)
                .onSuccess {
                    getCategoryList()
                }.onFailure {
                    showEmpty()
                }
        }
    }
}