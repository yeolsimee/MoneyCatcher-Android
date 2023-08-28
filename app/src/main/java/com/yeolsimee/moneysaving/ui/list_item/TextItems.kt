package com.yeolsimee.moneysaving.ui.list_item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Gray66
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.HorizontalSpacer


@Composable
fun SelectedItem(it: TextItem, selectCallback: (String) -> Unit) {
    Row(Modifier.padding(top = 6.dp)) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(GrayF0)
                .border(width = 1.5.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
                .padding(8.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {selectCallback(it.id)}
                )
        ) {
            PrText(
                text = it.name,
                fontWeight = FontWeight.W600,
                fontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
        8.HorizontalSpacer()
    }
}

@Composable
fun UnSelectedItem(it: TextItem, selectCallback: (String) -> Unit) {
    Row(Modifier.padding(top = 6.dp)) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .border(width = 1.5.dp, color = GrayF0, shape = RoundedCornerShape(4.dp))
                .padding(8.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {selectCallback(it.id)})
        ) {
            PrText(
                text = it.name,
                fontWeight = FontWeight.W500,
                fontSize = 13.sp,
                color = Gray66,
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
        8.HorizontalSpacer()
    }
}

@Preview(showBackground = true)
@Composable
fun SelectedItemPreview() {
    RoumoTheme {
        SelectedItem(
            it = TextItem("1", "\uD83D\uDCB0아껴쓰기"),
            selectCallback = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnSelectedItemPreview() {
    RoumoTheme {
        UnSelectedItem(
            it = TextItem("1", "\uD83D\uDCB0아껴쓰기"),
            selectCallback = {}
        )
    }
}