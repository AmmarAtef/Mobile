package com.example.weightapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PlanetWeightScreen()
                }
            }
        }
    }
}

@Composable
fun PlanetWeightScreen() {
    var weightInput by remember { mutableStateOf("") }
    var selectedPlanet by remember { mutableStateOf(1) }
    var result by remember { mutableStateOf("") }

    val planets = listOf(
        "Venus" to 0.78,
        "Mars" to 0.39,
        "Jupiter" to 2.65,
        "Saturn" to 1.17,
        "Uranus" to 1.05,
        "Neptune" to 1.23
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = weightInput,
            onValueChange = { weightInput = it },
            label = { Text("Enter weight in pounds") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Choose a planet:")
        planets.forEachIndexed { index, planet ->
            Row(modifier = Modifier.fillMaxWidth()) {
                RadioButton(
                    selected = selectedPlanet == index + 1,
                    onClick = { selectedPlanet = index + 1 }
                )
                Text(text = "${index + 1}. ${planet.first}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val earthWeight = weightInput.toDoubleOrNull()
            if (earthWeight != null && selectedPlanet in 1..planets.size) {
                val (planetName, gravity) = planets[selectedPlanet - 1]
                val planetWeight = earthWeight * gravity
                result = "Your weight on $planetName is %.2f pounds".format(planetWeight)
            } else {
                result = "Please enter a valid weight and select a planet."
            }
        }) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = result)
    }
}