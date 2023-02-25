package com.yeolsimee.moneysaving

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.yeolsimee.moneysaving.ui.theme.MoneySavingTheme
import com.yeolsimee.moneysaving.view.sample.SampleSideEffect
import com.yeolsimee.moneysaving.view.sample.SampleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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

                    Column(verticalArrangement = Arrangement.Center) {
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
                                    e.printStackTrace()
                                }
                            })
                        Text(text = state.toString())
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
    MoneySavingTheme {
        Greeting("Android")
    }
}