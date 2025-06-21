package com.example.problem3

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.problem3.ui.theme.Problem3Theme

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

data class CartItem(
    val name: String,
    val price: Double
)

data class Product(val name: String, val price: String, val emoji: String)
data class Promotion(val title: String, val color: Color, val emoji: String = "")

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Problem3Theme {
                GroceryAssistantApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryAssistantApp() {
    var selectedTab by remember { mutableStateOf(0) }

    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Cart", Icons.Default.ShoppingCart, "cart"),
        BottomNavItem("Profile", Icons.Default.Person, "profile")
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color.Black,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> HomeScreen(Modifier.padding(paddingValues))
            1 -> CartScreen(Modifier.padding(paddingValues))
            2 -> ProfileScreen(Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
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
fun CartScreen(modifier: Modifier = Modifier) {
    val cartItems = listOf(
        CartItem("Bananas", 0.99),
        CartItem("Apples", 4.49),
        CartItem("Carrots", 1.79),
        CartItem("Milk", 3.19)
    )

    val total = cartItems.sumOf { it.price }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Cart",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        cartItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "$${String.format("%.2f", item.price)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Divider(
                color = Color.Gray.copy(alpha = 0.3f),
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$${String.format("%.2f", total)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFF2196F3)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "JD",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Text(
            text = "John Doe",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "ammar@gmail.com",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileInfoCard("📍", "Address", "213 State St, Harrisburg, PA")
        ProfileInfoCard("📞", "Phone", "+1 (641) -233-9894")]
        ProfileInfoCard("🎯", "Loyalty Points", "1,250 points")
        ProfileInfoCard("📦", "Orders", "24 total orders")
        ProfileInfoCard("⭐", "Member Since", "January 2022")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2196F3)
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

@Composable
fun ProfileInfoCard(icon: String, title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Problem3Theme {
        GroceryAssistantApp()
    }
}