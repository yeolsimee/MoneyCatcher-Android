package com.yeolsimee.moneysaving.view.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import com.yeolsimee.moneysaving.ui.appbar.TopBackButtonTitleAppBar
import com.yeolsimee.moneysaving.ui.dialog.TwoButtonOneTitleDialog
import com.yeolsimee.moneysaving.ui.routine.EmptyRoutine
import com.yeolsimee.moneysaving.ui.side_effect.ApiCallSideEffect
import com.yeolsimee.moneysaving.ui.theme.Black33
import com.yeolsimee.moneysaving.ui.theme.DismissRed
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.utils.VerticalSpacer
import com.yeolsimee.moneysaving.utils.addFocusCleaner
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryUpdateScreen(
    onBackPressed: () -> Unit = {},
    categoryList: MutableList<TextItem> = mutableListOf(),
    sideEffect: State<ApiCallSideEffect>,
    onCategoryUpdate: (TextItem) -> Unit = {},
    onDelete: (TextItem) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
    ) {
        TopBackButtonTitleAppBar(text = "카테고리 수정", onClick = { onBackPressed() })
        21.VerticalSpacer()
        val targetCategory = remember { mutableStateOf<TextItem?>(null) }
        Column(
            Modifier
                .padding(horizontal = 28.dp)
                .scrollable(
                    state = rememberScrollState(),
                    orientation = Orientation.Vertical
                )
        ) {
            val currentCategory: MutableState<TextItem?> = remember { mutableStateOf(null) }
            for (category in categoryList) {

                val deleteDialogState = remember { mutableStateOf(false) }

                val swipeState = getCategorySwipeState(deleteDialogState, targetCategory, category)

                Column {
                    SwipeToDismiss(
                        state = swipeState,
                        background = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DismissRed)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.icon_trash),
                                    contentDescription = "루틴 지우기",
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 20.dp)
                                )
                            }
                        },
                        directions = setOf(DismissDirection.EndToStart),
                        dismissContent = {
                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(
                                        width = 2.5.dp, color = GrayF0,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .background(Color.White)
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                val tempName = remember { mutableStateOf(category.name) }

                                BasicTextField(
                                    value = tempName.value,
                                    textStyle = TextStyle(
                                        fontWeight = FontWeight.W700,
                                        fontSize = 15.sp,
                                        color = Black33,
                                    ),
                                    onValueChange = {
                                        tempName.value = it
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = {
                                        onCategoryUpdate(TextItem(category.id, tempName.value))
                                    }),
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                        .focusRequester(focusRequester)
                                        .onFocusChanged {
                                            if (it.hasFocus) {
                                                currentCategory.value = category
                                            } else {
                                                if (currentCategory.value != null) {
                                                    onCategoryUpdate(
                                                        TextItem(
                                                            currentCategory.value!!.id,
                                                            tempName.value
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                )
                            }
                        }
                    )
                    8.VerticalSpacer()
                }

                val scope = rememberCoroutineScope()
                if (deleteDialogState.value) {
                    TwoButtonOneTitleDialog(
                        text = "해당 카테고리를 삭제하시겠습니까?",
                        dialogState = deleteDialogState,
                        onConfirmClick = {
                            onDelete(targetCategory.value!!)
                        },
                        onCancelClick = {
                            scope.launch { swipeState.reset() }
                        }
                    )
                }
            }
        }
    }

    if (sideEffect.value == ApiCallSideEffect.Empty) {
        Box {
            EmptyRoutine(hasSubText = false)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun getCategorySwipeState(
    deleteDialogState: MutableState<Boolean>,
    targetCategory: MutableState<TextItem?>,
    category: TextItem
): DismissState {
    val swipeState = rememberDismissState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                deleteDialogState.value = true
                targetCategory.value = category
            }
            false
        }
    )
    return swipeState
}

@Preview(showBackground = true)
@Composable
fun CategoryUpdateScreenPreview() {
    CategoryUpdateScreen(
        categoryList = remember {
            mutableListOf(
                TextItem("1", "테스트1"),
                TextItem("2", "테스트2"),
                TextItem("3", "테스트3"),
                TextItem("4", "테스트4"),
            )
        },
        sideEffect = remember { mutableStateOf(ApiCallSideEffect.Loading) }
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryUpdateScreenEmptyPreview() {
    CategoryUpdateScreen(
        categoryList = remember {
            mutableListOf()
        },
        sideEffect = remember { mutableStateOf(ApiCallSideEffect.Empty) }
    )
}