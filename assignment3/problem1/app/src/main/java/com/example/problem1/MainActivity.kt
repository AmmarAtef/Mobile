package com.example.problem1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.problem1.ui.theme.Problem1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Problem1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GroceryStoreApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class Product(val name: String, val price: String, val emoji: String)
data class Promotion(val title: String, val color: Color, val emoji: String = "")

@Composable
fun GroceryStoreApp(modifier: Modifier = Modifier) {
    val promotions = listOf(
        Promotion("Special\nOffer", Color(0xFF9C27B0)),
        Promotion("50%", Color(0xFFFF5722), "🍊"),
        Promotion("Sale", Color(0xFF4CAF50))
    )

    val fruits = listOf(
        Product("Apple", "$0.99", "🍎"),
        Product("Banana", "$5.59", "🍌"),
        Product("Strawberry", "$1.99", "🍓"),
        Product("Orange", "$0.79", "🍊")
    )

    val vegetables = listOf(
        Product("Broccoli", "$1.49", "🥦"),
        Product("Carrot", "$0.89", "🥕")
    )

    val categories = listOf("Promotions", "Fruits", "Vegetables", "Bakery", "Beverages", "Dairy", "Meat", "Snacks")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            when (category) {
                "Promotions" -> {
                    Text(
                        text = "Promotions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(promotions) { promotion ->
                            PromotionCard(promotion)
                        }
                    }
                }
                "Fruits" -> {
                    Text(
                        text = "Fruits",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(fruits) { fruit ->
                            ProductCard(fruit)
                        }
                    }
                }
                "Vegetables" -> {
                    Text(
                        text = "Vegetables",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        vegetables.forEach { vegetable ->
                            VegetableListItem(vegetable)
                        }
                    }
                }
                else -> {
                    Text(
                        text = category,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PromotionCard(promotion: Promotion) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = promotion.color)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (promotion.emoji.isNotEmpty()) {
                    Text(
                        text = promotion.emoji,
                        fontSize = 24.sp
                    )
                }
                Text(
                    text = promotion.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = product.emoji,
                fontSize = 32.sp
            )
            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                text = product.price,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun VegetableListItem(vegetable: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = vegetable.emoji,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column {
                Text(
                    text = vegetable.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = vegetable.price,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroceryStorePreview() {
    Problem1Theme {
        GroceryStoreApp()
    }
}