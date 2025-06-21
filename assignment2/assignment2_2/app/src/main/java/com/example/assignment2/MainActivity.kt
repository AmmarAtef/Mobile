package com.example.assignment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DinnerDeciderApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DinnerDeciderApp() {
    var foodList by remember {
        mutableStateOf(arrayListOf("Hamburger", "Pizza", "Mexican", "American", "Chinese"))
    }

    var textFieldValue by remember { mutableStateOf("") }
    var decidedFood by remember { mutableStateOf("What's for dinner?") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Dinner Decider",
            fontSize = 24.sp
        )

        Text(
            text = decidedFood,
            fontSize = 18.sp
        )

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("Enter food name") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (textFieldValue.isNotBlank()) {
                    foodList.add(textFieldValue.trim())
                    textFieldValue = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ADD FOOD")
        }

        Button(
            onClick = {
                if (foodList.isNotEmpty()) {
                    val randomIndex = Random.nextInt(foodList.size)
                    decidedFood = foodList[randomIndex]
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("DECIDE!")
        }

        Text(
            text = "Options: ${foodList.joinToString(", ")}",
            fontSize = 14.sp
        )
    }
}