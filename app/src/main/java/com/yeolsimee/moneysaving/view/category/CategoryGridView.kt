@file:OptIn(ExperimentalLayoutApi::class)

package com.yeolsimee.moneysaving.view.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.yeolsimee.moneysaving.ui.dialog.AddCategoryDialog
import com.yeolsimee.moneysaving.ui.list_item.SelectedItem
import com.yeolsimee.moneysaving.ui.list_item.UnSelectedItem
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme

@Composable
fun CategoryGridView(
    categories: MutableList<TextItem>,
    selectedId: String,
    addCategoryState: MutableState<Boolean>,
    selectCallback: (String) -> Unit,
    addCallback: (String) -> Unit = {},
) {
    Column(modifier = Modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.image_tag),
                contentDescription = "루틴 카테고리 설정"
            )
            Spacer(Modifier.width(4.dp))
            PrText(
                text = "루틴의 카테고리를 설정해주세요", fontWeight = FontWeight.W700, fontSize = 15.sp
            )
        }
        Spacer(Modifier.height(11.dp))
        FlowRow {
            categories.forEach {
                if (selectedId == it.id) {
                    SelectedItem(it, selectCallback)
                } else {
                    UnSelectedItem(it, selectCallback)
                }
            }
            if (categories.size < 10) {
                Box(Modifier.padding(top = 6.dp)) {
                    Box(
                        Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Black)
                            .clickable(interactionSource = remember {
                                MutableInteractionSource()
                            }, indication = null, onClick = {
                                addCategoryState.value = true
                            })
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "카테고리 추가",
                            tint = Color.White,
                            modifier = Modifier
                                .width(16.dp)
                                .height(16.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                AddCategoryDialog(
                    dialogState = addCategoryState,
                    onConfirmClick = { addCallback(it) })
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 400)
@Composable
fun SelectCategoryPreview() {
    RoumoTheme {
        val selectedCategoryId = remember { mutableStateOf("1") }
        val addCategoryState = remember { mutableStateOf(false) }
        CategoryGridView(
            mutableListOf(
                TextItem("1", "💰아껴쓰기"),
                TextItem("2", "주린이 성장일기"),
                TextItem("3", "임티는 사용자 자유"),
                TextItem("4", "열네글자까지들어가요일이삼사")
            ),
            selectedId = "1",
            addCategoryState = addCategoryState,
            selectCallback = {
                selectedCategoryId.value = it
            },
            addCallback = {}
        )
    }
}