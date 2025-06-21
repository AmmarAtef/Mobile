package com.example.problem1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.problem1.ui.theme.Problem1Theme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Problem1Theme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var layoutBackgroundColor by remember { mutableStateOf(Color.White) }
    var imageBackgroundColor by remember { mutableStateOf(Color.LightGray) }

    val backgroundColors = listOf(
        Color.White, Color(0xFFFFE4E1), Color(0xFFE1F5FE), Color(0xFFF3E5F5),
        Color(0xFFE8F5E8), Color(0xFFFFF3E0), Color(0xFFE0F2F1), Color(0xFFFCE4EC),
        Color(0xFFEDE7F6), Color(0xFFE3F2FD)
    )

    val imageBackgroundColors = listOf(
        Color.LightGray, Color(0xFFFFCDD2), Color(0xFFBBDEFB), Color(0xFFE1BEE7),
        Color(0xFFC8E6C9), Color(0xFFFFE0B2), Color(0xFFB2DFDB), Color(0xFFF8BBD9),
        Color(0xFFD1C4E9), Color(0xFFFFAB91)
    )

    fun getRandomBackgroundColor(): Color {
        return backgroundColors[Random.nextInt(backgroundColors.size)]
    }

    fun getRandomImageBackgroundColor(): Color {
        return imageBackgroundColors[Random.nextInt(imageBackgroundColors.size)]
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MyTestApp",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EA)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(layoutBackgroundColor)
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Maharishi International University",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE91E63),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            Button(
                onClick = {
                    layoutBackgroundColor = getRandomBackgroundColor()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EA)
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "SetBackground",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(imageBackgroundColor)
                    .clickable {
                        imageBackgroundColor = getRandomImageBackgroundColor()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.maharishi_logo),
                    contentDescription = "Maharishi University Logo",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Click the button to change layout background\nClick the image to change image background",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Problem1Theme {
        MainScreen()
    }
}