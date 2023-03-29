package com.yeolsimee.moneysaving.view.sample

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.theme.MoneySavingTheme
import com.yeolsimee.moneysaving.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : ComponentActivity() {

    private val viewModel: SampleViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneySavingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state by viewModel.container.stateFlow.collectAsState()
                    val text = remember { mutableStateOf("12") }

                    val dialogState = remember { mutableStateOf(false) }
                    YearMonthDialog(dialogState)

                    Column(verticalArrangement = Arrangement.Center) {
                        Button(onClick = {
                            dialogState.value = !dialogState.value
                        }) {
                            Text(text = "Date Picker Dialog")
                        }
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
                        Text(text = state.toString())
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

    @SuppressLint("InflateParams")
    @Composable
    private fun YearMonthDialog(dialogState: MutableState<Boolean>) {
        if (dialogState.value) {
            Dialog(onDismissRequest = {
                dialogState.value = false
            }) {
                AndroidView(factory = { context ->
                    val view =
                        LayoutInflater.from(context).inflate(R.layout.year_month_picker, null)

                    val yearPicker = view.findViewById<NumberPicker>(R.id.picker_year)
                    val monthPicker = view.findViewById<NumberPicker>(R.id.picker_month)
                    val cancelButton = view.findViewById<Button>(R.id.btn_cancel)
                    val confirmButton = view.findViewById<Button>(R.id.btn_confirm)

                    yearPicker.minValue = 2023
                    yearPicker.maxValue = 2099
                    yearPicker.value = 2023

                    monthPicker.minValue = 1
                    monthPicker.maxValue = 12
                    monthPicker.value = 3

                    cancelButton.setOnClickListener {
                        dialogState.value = false
                    }

                    confirmButton.setOnClickListener {
                        Toast.makeText(applicationContext, "${yearPicker.value}년 ${monthPicker.value}월", Toast.LENGTH_SHORT).show()
                        dialogState.value = false
                    }

                    view
                })
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
    MoneySavingTheme {
        Greeting("Android")
    }
}