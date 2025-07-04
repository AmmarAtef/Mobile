package com.example.assignment4



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Data Models
data class MarsPhoto(
    @SerializedName("id") val id: Int,
    @SerializedName("img_src") val imgSrc: String,
    @SerializedName("earth_date") val earthDate: String,
    @SerializedName("sol") val sol: Int,
    @SerializedName("camera") val camera: Camera,
    @SerializedName("rover") val rover: Rover
)

data class Camera(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("rover_id") val roverId: Int,
    @SerializedName("full_name") val fullName: String
)

data class Rover(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("landing_date") val landingDate: String,
    @SerializedName("launch_date") val launchDate: String,
    @SerializedName("status") val status: String
)

data class MarsPhotosResponse(
    @SerializedName("photos") val photos: List<MarsPhoto>
)

// UI State
sealed class MarsUiState {
    data object Loading : MarsUiState()
    data class Success(val photos: List<MarsPhoto>) : MarsUiState()
    data class Error(val message: String) : MarsUiState()
}

// Retrofit API Interface
interface MarsApiService {
    @GET("rovers/curiosity/photos")
    suspend fun getPhotos(
        @Query("sol") sol: Int = 1000,
        @Query("api_key") apiKey: String = "DEMO_KEY"
    ): Response<MarsPhotosResponse>

    @GET("rovers/opportunity/photos")
    suspend fun getOpportunityPhotos(
        @Query("sol") sol: Int = 1000,
        @Query("api_key") apiKey: String = "DEMO_KEY"
    ): Response<MarsPhotosResponse>

    @GET("rovers/spirit/photos")
    suspend fun getSpiritPhotos(
        @Query("sol") sol: Int = 1000,
        @Query("api_key") apiKey: String = "DEMO_KEY"
    ): Response<MarsPhotosResponse>
}

// Repository
class MarsPhotosRepository {
    private val marsApi = Retrofit.Builder()
        .baseUrl("https://api.nasa.gov/mars-photos/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MarsApiService::class.java)

    suspend fun getMarsPhotos(rover: String = "curiosity", sol: Int = 1000): List<MarsPhoto> {
        return try {
            val response = when (rover.lowercase()) {
                "curiosity" -> marsApi.getPhotos(sol = sol)
                "opportunity" -> marsApi.getOpportunityPhotos(sol = sol)
                "spirit" -> marsApi.getSpiritPhotos(sol = sol)
                else -> marsApi.getPhotos(sol = sol)
            }

            if (response.isSuccessful) {
                response.body()?.photos ?: emptyList()
            } else {
                // Return mock data for demo purposes
                getMockMarsPhotos(rover)
            }
        } catch (e: Exception) {
            // Return mock data for demo purposes
            getMockMarsPhotos(rover)
        }
    }

    private fun getMockMarsPhotos(rover: String): List<MarsPhoto> {
        val baseUrls = listOf(
            "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/fcam/FLB_486265257EDR_F0481570FHAZ00323M_.JPG",
            "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/fcam/FRB_486265257EDR_F0481570FHAZ00323M_.JPG",
            "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/rcam/RLB_486265291EDR_F0481570RHAZ00323M_.JPG",
            "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/rcam/RRB_486265291EDR_F0481570RHAZ00323M_.JPG"
        )

        return (1..20).map { index ->
            MarsPhoto(
                id = index,
                imgSrc = baseUrls[index % baseUrls.size],
                earthDate = "2015-05-30",
                sol = 1000,
                camera = Camera(
                    id = index,
                    name = listOf("FHAZ", "RHAZ", "NAVCAM", "MARDI")[index % 4],
                    roverId = 1,
                    fullName = listOf(
                        "Front Hazard Avoidance Camera",
                        "Rear Hazard Avoidance Camera",
                        "Navigation Camera",
                        "Mars Descent Imager"
                    )[index % 4]
                ),
                rover = Rover(
                    id = 1,
                    name = rover.replaceFirstChar { it.uppercase() },
                    landingDate = "2012-08-05",
                    launchDate = "2011-11-26",
                    status = "active"
                )
            )
        }
    }
}

// ViewModel
class MarsPhotosViewModel : ViewModel() {
    private val repository = MarsPhotosRepository()

    var uiState by mutableStateOf<MarsUiState>(MarsUiState.Loading)
        private set

    var selectedRover by mutableStateOf("Curiosity")
        private set

    var selectedSol by mutableStateOf(1000)
        private set

    init {
        getMarsPhotos()
    }

    fun getMarsPhotos() {
        viewModelScope.launch {
            uiState = MarsUiState.Loading
            try {
                val photos = repository.getMarsPhotos(selectedRover, selectedSol)
                uiState = MarsUiState.Success(photos)
            } catch (e: Exception) {
                uiState = MarsUiState.Error("Failed to load Mars photos: ${e.message}")
            }
        }
    }

    fun updateRover(rover: String) {
        selectedRover = rover
        getMarsPhotos()
    }

    fun updateSol(sol: Int) {
        selectedSol = sol
        getMarsPhotos()
    }
}

// Main Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarsPhotosTheme {
                MarsPhotosApp()
            }
        }
    }
}

// Theme
@Composable
fun MarsPhotosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFCD853F),
            secondary = Color(0xFFD2691E),
            background = Color(0xFF1A1A1A),
            surface = Color(0xFF2D2D2D),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color.White,
            onSurface = Color.White
        ),
        content = content
    )
}

// Main App Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarsPhotosApp() {
    val viewModel: MarsPhotosViewModel = viewModel()
    var showFilters by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2C1810),
                        Color(0xFF1A1A1A),
                        Color(0xFF000000)
                    )
                )
            )
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Mars Photos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${viewModel.selectedRover} • Sol ${viewModel.selectedSol}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFCD853F)
            ),
            actions = {
                IconButton(onClick = { showFilters = true }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filters",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { viewModel.getMarsPhotos() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.White
                    )
                }
            }
        )

        // Content based on UI state
        when (val state = viewModel.uiState) {
            is MarsUiState.Loading -> {
                LoadingScreen()
            }
            is MarsUiState.Success -> {
                MarsPhotosGrid(photos = state.photos)
            }
            is MarsUiState.Error -> {
                ErrorScreen(
                    message = state.message,
                    onRetry = { viewModel.getMarsPhotos() }
                )
            }
        }
    }

    // Filter Dialog
    if (showFilters) {
        FilterDialog(
            currentRover = viewModel.selectedRover,
            currentSol = viewModel.selectedSol,
            onRoverChange = { viewModel.updateRover(it) },
            onSolChange = { viewModel.updateSol(it) },
            onDismiss = { showFilters = false }
        )
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = Color(0xFFCD853F),
                strokeWidth = 6.dp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Loading Mars Photos...",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Connecting to NASA API",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = "Error",
                tint = Color(0xFFCD853F),
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Houston, we have a problem!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFCD853F)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Try Again")
            }
        }
    }
}

@Composable
fun MarsPhotosGrid(photos: List<MarsPhoto>) {
    var selectedPhoto by remember { mutableStateOf<MarsPhoto?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(photos) { photo ->
            MarsPhotoCard(
                photo = photo,
                onClick = { selectedPhoto = photo }
            )
        }
    }

    // Full screen photo dialog
    selectedPhoto?.let { photo ->
        PhotoDetailDialog(
            photo = photo,
            onDismiss = { selectedPhoto = null }
        )
    }
}

@Composable
fun MarsPhotoCard(
    photo: MarsPhoto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D2D2D)
        )
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo.imgSrc)
                    .crossfade(true)
                    .build(),
                contentDescription = "Mars photo from ${photo.camera.name}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                error = painterResource(android.R.drawable.ic_menu_gallery)
            )

            // Overlay with photo info
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            // Photo details
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = photo.camera.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Sol ${photo.sol}",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun PhotoDetailDialog(
    photo: MarsPhoto,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2D2D2D)
            )
        ) {
            Column {
                // Photo
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photo.imgSrc)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Mars photo detail",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Details
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = photo.camera.fullName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    PhotoDetailRow("Rover", photo.rover.name)
                    PhotoDetailRow("Sol", photo.sol.toString())
                    PhotoDetailRow("Earth Date", photo.earthDate)
                    PhotoDetailRow("Camera", photo.camera.name)
                    PhotoDetailRow("Status", photo.rover.status.replaceFirstChar { it.uppercase() })

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFCD853F)
                        )
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
fun FilterDialog(
    currentRover: String,
    currentSol: Int,
    onRoverChange: (String) -> Unit,
    onSolChange: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val rovers = listOf("Curiosity", "Opportunity", "Spirit")
    var selectedRover by remember { mutableStateOf(currentRover) }
    var solText by remember { mutableStateOf(currentSol.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2D2D2D)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Filter Photos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Rover selection
                Text(
                    text = "Rover",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                rovers.forEach { rover ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedRover = rover }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedRover == rover,
                            onClick = { selectedRover = rover },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFCD853F)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = rover,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sol input
                Text(
                    text = "Sol (Martian Day)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = solText,
                    onValueChange = { solText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter Sol number") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFCD853F),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            onRoverChange(selectedRover)
                            solText.toIntOrNull()?.let { sol ->
                                onSolChange(sol)
                            }
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFCD853F)
                        )
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}