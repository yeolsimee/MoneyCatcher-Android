package com.yeolsimee.moneysaving.view.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yeolsimee.moneysaving.ui.theme.MoneyCatcherTheme
import com.yeolsimee.moneysaving.view.calendar.CalendarScreen
import com.yeolsimee.moneysaving.view.calendar.CalendarViewModel
import com.yeolsimee.moneysaving.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : ComponentActivity() {

    private val viewModel: SampleViewModel by viewModels()
    private val calendarViewModel: CalendarViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyCatcherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state by viewModel.container.stateFlow.collectAsState()
                    val text = remember { mutableStateOf("12") }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CalendarScreen(calendarViewModel)

                        OutlinedTextField(
                            value = text.value,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                            onValueChange = {
                                try {
                                    if (it.isNotEmpty() && !it.contains(" ")) {
                                        val round = it.toInt()
                                        viewModel.getData(round)
                                        text.value = round.toString()
                                    }
                                } catch (e: java.lang.NumberFormatException) {
                                    Log.e("test", "${e.message}")
                                    e.printStackTrace()
                                }
                            })
                        Text(text = state.toString(), textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = {
                            val intent = Intent(this@SampleActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text(text = "로그인 테스트")
                        }
                    }
                    SetSideEffect(this, viewModel)
                }
            }
        }
    }


}

@Composable
private fun SetSideEffect(activity: Activity, viewModel: SampleViewModel) {
    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            Toast.makeText(
                activity.applicationContext, (it as SampleSideEffect.Toast).test, Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoneyCatcherTheme {
        Greeting("Android")
    }
}