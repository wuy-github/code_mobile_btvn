package com.example.authentication_google

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.authentication_google.R
import com.example.authentication_google.ui.theme.Authentication_googleTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import androidx.activity.compose.BackHandler // THÊM DÒNG NÀY

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Authentication_googleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// =========================================================================
//                  Hệ thống điều hướng chính của ứng dụng
// =========================================================================
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = remember { FirebaseAuth.getInstance() } // Sử dụng remember để giữ instance của FirebaseAuth

    // Xác định màn hình bắt đầu dựa trên trạng thái đăng nhập của người dùng
    val startDestination = if (auth.currentUser != null) "profile" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onSignInSuccess = {
                    // Khi đăng nhập thành công, điều hướng đến màn hình profile
                    navController.navigate("profile") {
                        // Xóa màn hình đăng nhập khỏi back stack để người dùng không thể quay lại bằng nút Back
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("profile") {
            ProfileScreen(
                onBackClick = {
                    // Đăng xuất người dùng khi nhấn nút Back trên màn hình Profile
                    auth.signOut()
                    // Quan trọng: Thêm GoogleSignIn.getClient(context, gso).signOut() để đăng xuất khỏi tài khoản Google
                    // Đây là bước bổ sung để cho phép đăng nhập tài khoản Google khác dễ dàng hơn.
                    val context = navController.context as Activity // Lấy context từ NavController
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    googleSignInClient.signOut().addOnCompleteListener {
                        Toast.makeText(context, "Đã đăng xuất khỏi Google.", Toast.LENGTH_SHORT).show()
                    }

                    // Quay lại màn hình đăng nhập và xóa màn hình profile khỏi back stack
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                }
            )
        }
    }
}

// =========================================================================
//                  Composable cho Màn hình Đăng nhập (LoginScreen)
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSignInSuccess: () -> Unit // Callback khi đăng nhập thành công
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    // Cấu hình Google Sign-In Options
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)) // Lấy từ strings.xml
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    // Launcher để xử lý kết quả từ Activity của Google Sign-In
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { idToken ->
                    // Đổi Google ID Token lấy được thành Firebase Credential
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    // Đăng nhập Firebase với Credential
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { firebaseAuthTask ->
                            if (firebaseAuthTask.isSuccessful) {
                                // Đăng nhập Firebase thành công
                                Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                                onSignInSuccess() // Gọi callback để điều hướng
                            } else {
                                // Đăng nhập Firebase thất bại
                                Toast.makeText(context, "Đăng nhập Firebase thất bại: ${firebaseAuthTask.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            } catch (e: ApiException) {
                // Đăng nhập Google thất bại (ví dụ: người dùng hủy, lỗi mạng)
                Toast.makeText(context, "Đăng nhập Google thất bại: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            // Đăng nhập Google bị hủy
            Toast.makeText(context, "Đăng nhập Google bị hủy.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.uth_logo), // Đảm bảo bạn có file uth_logo.png/xml trong drawable
            contentDescription = "UTH Logo",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "SmartTasks", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Welcome", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Ready to explore? Log in to get started.", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                // Khởi động luồng đăng nhập Google
                val signInIntent = googleSignInClient.signInIntent
                signInLauncher.launch(signInIntent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "SIGN IN WITH GOOGLE")
        }
    }
}

// =========================================================================
//                  Composable cho Màn hình Profile (ProfileScreen)
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit
) {
    // Lấy thông tin người dùng hiện tại từ Firebase
    val currentUser: FirebaseUser? = remember { FirebaseAuth.getInstance().currentUser }

    // =========================================================================================
    // THÊM BackHandler VÀO ĐÂY ĐỂ BẮT SỰ KIỆN NÚT BACK VẬT LÝ
    // =========================================================================================
    BackHandler(enabled = true) {
        // Khi nút Back vật lý được nhấn, gọi hành động đăng xuất
        onBackClick()
    }
    // =========================================================================================

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Hiển thị ảnh đại diện
            currentUser?.photoUrl?.let { photoUrl ->
                Image(
                    painter = rememberAsyncImagePainter(photoUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No Photo", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            ProfileInfoRow(label = "Name", value = currentUser?.displayName ?: "N/A")
            ProfileInfoRow(label = "Email", value = currentUser?.email ?: "N/A")
            ProfileInfoRow(label = "Date of Birth", value = "2/20/1985")
            Spacer(modifier = Modifier.height(32.dp))

            // Nút Back/Logout
            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout") // ĐỔI THÀNH "Logout" để rõ ràng hơn
            }
        }
    }
}

// Composable phụ trợ cho ProfileScreen
@Composable
fun ProfileInfoRow(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp))
    }
}

// =========================================================================
//                  Phần Preview (Nếu bạn muốn giữ lại)
// =========================================================================
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Authentication_googleTheme {
        LoginScreen(onSignInSuccess = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Authentication_googleTheme {
        ProfileScreen(onBackClick = {})
    }
}