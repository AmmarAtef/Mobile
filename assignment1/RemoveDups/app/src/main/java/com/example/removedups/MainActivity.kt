package com.example.removedups


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RemoveDupsScreen()
                }
            }
        }
    }
}

@Composable
fun RemoveDupsScreen() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter comma-separated words") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val words = input.split(",").map { it.trim() }.toTypedArray()
            result = removeDups(words)
        }) {
            Text("Remove Duplicates")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val output = result.joinToString(", ")
        Text(text = "Result: $output")
    }
}

fun removeDups(input: Array<String>): List<String> {
    val seen = mutableSetOf<String>()
    val result = mutableListOf<String>()
    for (item in input) {
        if (item !in seen) {
            seen.add(item)
            result.add(item)
        }
    }
    return result
}
