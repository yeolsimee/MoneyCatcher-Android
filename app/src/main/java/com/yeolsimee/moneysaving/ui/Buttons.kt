package com.yeolsimee.moneysaving.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.onClick
import com.yeolsimee.moneysaving.view.home.RoutineStateViewModel

@Composable
fun AnimatedToggleButton(
    routineStateViewModel: RoutineStateViewModel = hiltViewModel(),
    routineViewState: Boolean = false
) {
    Image(
        painter = painterResource(id = if (routineViewState) R.drawable.toggle_on else R.drawable.toggle_off),
        contentDescription = "루틴 UI 변경",
        modifier = Modifier
            .wrapContentSize()
            .onClick {
                routineStateViewModel.setRoutineState(!routineViewState)
            }
    )
}

@Composable
@Preview(showBackground = true)
fun AnimatedToggleButtonPreview() {
    RoumoTheme {
        AnimatedToggleButton()
    }
}