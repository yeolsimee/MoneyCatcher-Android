package com.yeolsimee.moneysaving.view.category

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeolsimee.moneysaving.ui.side_effect.ApiCallSideEffect
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.collectAsStateWithLifecycleRemember
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryUpdateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val categoryViewModel: CategoryViewModel = hiltViewModel()
            val list = categoryViewModel.container.stateFlow.collectAsStateWithLifecycleRemember(
                mutableListOf()
            )

            val sideEffect =
                categoryViewModel.container.sideEffectFlow.collectAsStateWithLifecycleRemember(
                    initial = ApiCallSideEffect.Loading
                )

            RoumoTheme(navigationBarColor = Color.Black) {
                CategoryUpdateScreen(
                    onBackPressed = {
                        setResult(RESULT_OK)
                        finish()
                    },
                    categoryList = list.value,
                    sideEffect = sideEffect,
                    onCategoryUpdate = {
                        categoryViewModel.update(it)
                    },
                    onDelete = {
                        categoryViewModel.delete(it)
                    }
                )
            }
        }
    }
}