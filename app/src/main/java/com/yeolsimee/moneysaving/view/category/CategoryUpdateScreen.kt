@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.view.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.appbar.TopBackButtonTitleAppBar
import com.yeolsimee.moneysaving.ui.dialog.TwoButtonOneTitleDialog
import com.yeolsimee.moneysaving.ui.routine.EmptyRoutine
import com.yeolsimee.moneysaving.ui.theme.Black33
import com.yeolsimee.moneysaving.ui.theme.DismissRed
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import kotlinx.coroutines.launch

@Composable
fun CategoryUpdateScreen(
    onBackPressed: () -> Unit = {},
    categoryList: MutableList<TextItem> = mutableListOf(),
    onDelete: (TextItem) -> Unit = {}
) {

    val deleteDialogState = remember { mutableStateOf(false) }
    val targetCategory = remember { mutableStateOf<TextItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBackButtonTitleAppBar(text = "카테고리 수정", onClick = { onBackPressed() })
        Spacer(Modifier.height(21.dp))

        Column(
            Modifier
                .padding(horizontal = 28.dp)
                .scrollable(
                    state = rememberScrollState(),
                    orientation = Orientation.Vertical
                )
        ) {
            val scope = rememberCoroutineScope()

            for (category in categoryList) {
                Column(modifier = Modifier) {
                    val swipeState = setCategorySwipeState(
                        category = category,
                        onItemDelete = {
                            deleteDialogState.value = true
                            targetCategory.value = category
                        }
                    )

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
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White)
                                    .border(
                                        width = 2.5.dp, color = GrayF0,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            ) {
                                PrText(
                                    text = category.name,
                                    fontWeight = FontWeight.W700,
                                    fontSize = 15.sp,
                                    color = Black33,
                                    modifier = Modifier.padding(top = 17.dp, start = 20.dp)
                                )
                            }
                        }
                    )
                    Spacer(Modifier.height(8.dp))

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

            if (categoryList.isEmpty()) {
                EmptyRoutine()
            }
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun setCategorySwipeState(
    category: TextItem,
    onItemDelete: (TextItem) -> Unit
) = rememberDismissState(
    confirmValueChange = { dismissValue ->
        if (dismissValue == DismissValue.DismissedToStart) {
            onItemDelete(category)
            true
        } else {
            false
        }
    },
)

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
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryUpdateScreenEmptyPreview() {
    CategoryUpdateScreen(
        categoryList = remember {
            mutableListOf()
        }
    )
}