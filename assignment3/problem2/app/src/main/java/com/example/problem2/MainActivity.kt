package com.example.problem2

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.problem2.ui.theme.Problem2Theme

data class GroceryItem(
    val name: String,
    val imageUrl: String,
    val price: Double,
    val description: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(imageUrl)
        parcel.writeDouble(price)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroceryItem> {
        override fun createFromParcel(parcel: Parcel): GroceryItem {
            return GroceryItem(parcel)
        }

        override fun newArray(size: Int): Array<GroceryItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class Product(val name: String, val price: String, val emoji: String)
data class Promotion(val title: String, val color: Color, val emoji: String = "")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Problem2Theme {
                GroceryApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryApp() {
    var selectedProduct by remember { mutableStateOf<GroceryItem?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (selectedProduct != null) {
                TopAppBar(
                    title = { Text("Detail Screen") },
                    navigationIcon = {
                        IconButton(onClick = { selectedProduct = null }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        if (selectedProduct == null) {
            GroceryStoreApp(
                modifier = Modifier.padding(innerPadding),
                onProductClick = { product -> selectedProduct = product }
            )
        } else {
            ProductDetailScreen(
                product = selectedProduct!!,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun GroceryStoreApp(
    modifier: Modifier = Modifier,
    onProductClick: (GroceryItem) -> Unit
) {
    val promotions = listOf(
        Promotion("Special\nOffer", Color(0xFF9C27B0)),
        Promotion("50%", Color(0xFFFF5722), "🍊"),
        Promotion("Sale", Color(0xFF4CAF50))
    )

    val fruits = listOf(
        GroceryItem("Apple", "🍎", 0.99, "Apples are a popular fruit, rich in fiber and vitamin C. They come in a variety of colors, including red, green, and yellow."),
        GroceryItem("Banana", "🍌", 5.59, "Sweet yellow bananas. Great source of potassium and natural energy. Perfect for smoothies and healthy snacks."),
        GroceryItem("Strawberry", "🍓", 1.99, "Juicy, sweet strawberries. Packed with antioxidants and vitamin C. Great for desserts and fresh eating."),
        GroceryItem("Orange", "🍊", 0.79, "Fresh citrus oranges. Bursting with vitamin C and natural sweetness. Perfect for fresh juice and snacking.")
    )

    val vegetables = listOf(
        GroceryItem("Broccoli", "🥦", 1.49, "Fresh green broccoli. Rich in vitamins K and C. Great for steaming, roasting, or stir-frying."),
        GroceryItem("Carrot", "🥕", 0.89, "Crisp orange carrots. High in beta-carotene and fiber. Perfect for snacking or cooking.")
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
                            ProductCard(
                                product = Product(fruit.name, "$${fruit.price}", fruit.imageUrl),
                                onClick = { onProductClick(fruit) }
                            )
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
                            VegetableListItem(
                                vegetable = Product(vegetable.name, "$${vegetable.price}", vegetable.imageUrl),
                                onClick = { onProductClick(vegetable) }
                            )
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
fun ProductDetailScreen(
    product: GroceryItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = product.name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier
                .size(200.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.imageUrl,
                    fontSize = 80.sp
                )
            }
        }

        Text(
            text = "Price: $${product.price}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )

        Text(
            text = product.description,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF666666),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text(
                text = "Add to Cart",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
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
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(120.dp)
            .clickable { onClick() },
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
fun VegetableListItem(
    vegetable: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
    Problem2Theme {
        GroceryApp()
    }
}