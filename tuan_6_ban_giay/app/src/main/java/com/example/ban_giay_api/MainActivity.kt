// XÓA HẾT CODE CŨ VÀ DÁN CODE NÀY VÀO

package com.example.ban_giay_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ban_giay_api.ui.theme.Ban_giay_apiTheme // Tên theme này phải khớp với dự án của bạn
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.text.NumberFormat
import java.util.Locale

// ===================================================================
// PHẦN 1: CÁC LỚP DỮ LIỆU (DATA MODELS)
// ===================================================================


data class Product(
    @SerializedName("name")
    val name: String,
    @SerializedName("des")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("imgURL")
    val imageUrl: String
)

// ===================================================================
// PHẦN 2: NETWORK (RETROFIT)
// ===================================================================
interface ApiService {
    @GET("m1/890655-872447-default/v2/product")
    suspend fun getProduct(): Product
}

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://mock.apidog.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// ===================================================================
// PHẦN 3: VIEWMODEL
// ===================================================================
data class ProductUiState(
    val product: Product? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class ProductViewModel : ViewModel() {
    private val _state = mutableStateOf(ProductUiState())
    val state: State<ProductUiState> = _state

    init {
        fetchProduct()
    }

    private fun fetchProduct() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val product = RetrofitInstance.api.getProduct()
                _state.value = _state.value.copy(
                    product = product,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Lỗi kết nối mạng: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun formatPrice(price: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        return format.format(price)
    }
}

// ===================================================================
// PHẦN 4: ACTIVITY CHÍNH VÀ UI (JETPACK COMPOSE)
// ===================================================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ban_giay_apiTheme { // Đảm bảo tên theme này khớp với file ui/theme/Theme.kt của bạn
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProductScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(productViewModel: ProductViewModel = viewModel()) {
    val uiState by productViewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product detail") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Xử lý nút quay lại sau */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            Text(
                text = "UTH SmartTasks",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0))
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                uiState.product?.let { product ->
                    ProductContent(product = product, viewModel = productViewModel)
                }
            }
        }
    }
}

@Composable
fun ProductContent(product: Product, viewModel: ProductViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(androidx.compose.foundation.rememberScrollState())
    ) {
        // Vùng chứa ảnh sản phẩm
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFFFF6B6B)), // Màu nền đỏ cam
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Vùng chứa thông tin sản phẩm
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = product.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Giá: ${viewModel.formatPrice(product.price)}",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dấu gạch ngang phân cách
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Text(
                text = product.description,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductScreenPreview() {
    Ban_giay_apiTheme {
        ProductScreen()
    }
}