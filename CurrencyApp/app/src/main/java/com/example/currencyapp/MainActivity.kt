package com.example.currencyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CurrencyConverterScreen()
                }
            }
        }
    }
}

@Composable
fun CurrencyConverterScreen() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter amount (e.g. 11.56)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val amount = input.toDoubleOrNull()
            result = if (amount != null) convertCurrency(amount) else "Invalid input"
        }) {
            Text("Convert")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = result)
    }
}

fun convertCurrency(amount: Double): String {
    var cents = (amount * 100).roundToInt()

    val dollars = cents / 100
    cents %= 100

    val quarters = cents / 25
    cents %= 25

    val dimes = cents / 10
    cents %= 10

    val nickels = cents / 5
    cents %= 5

    val pennies = cents

    return """
        Your amount $amount consists of:
        $dollars dollars
        $quarters quarters
        $dimes dimes
        $nickels nickels
        $pennies pennies
    """.trimIndent()
}