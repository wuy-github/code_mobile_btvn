// Thay bằng tên package chính xác trong dự án của bạn
package com.example.thuc_hanh_2_tuan_4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Định nghĩa màu sử dụng trong ứng dụng
val UthAppBlue = Color(0xFF007AFF)             // Màu xanh dương chính
val UthTextBlue = Color(0xFF00529B)            // Màu xanh đậm cho tiêu đề
val TitleTextColor = Color.Black               // Màu tiêu đề
val DescriptionTextColor = Color(0xFF6D6D72)   // Màu mô tả
val InactiveIndicatorColor = Color(0xFFE0E0E0) // Màu chấm không được chọn
val BackButtonBackgroundColor = Color(0xFFF0F0F0) // Nền nút quay lại

// Các màn hình của ứng dụng
enum class AppScreen {
    Splash,
    Onboarding
}

// Dữ liệu của mỗi trang giới thiệu (onboarding)
data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

// Activity chính của ứng dụng
class MainActivity : ComponentActivity() {

    // Danh sách các trang onboarding
    private val onboardingPagesList = listOf(
        OnboardingPage(R.drawable.bro, "Easy Time Management", "With management based on priority and daily tasks..."),
        OnboardingPage(R.drawable.bro2, "Increase Work Effectiveness", "Time management and prioritizing tasks will improve work..."),
        OnboardingPage(R.drawable.bro3, "Reminder Notification", "This app provides reminders so you don’t forget your tasks...")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Giao diện chính sử dụng MaterialTheme
            MaterialTheme {
                var currentScreen by remember { mutableStateOf(AppScreen.Splash) }

                // Giao diện chuyển đổi giữa splash và onboarding
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    when (currentScreen) {
                        AppScreen.Splash -> SplashScreen { currentScreen = AppScreen.Onboarding }
                        AppScreen.Onboarding -> OnboardingScreen(
                            pages = onboardingPagesList,
                            onGetStarted = { finish() } // Kết thúc Activity khi hoàn tất onboarding
                        )
                    }
                }
            }
        }
    }
}

// Màn hình Splash hiển thị logo trong 5 giây
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000L)
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.uth_logo),
                contentDescription = "UTH Logo",
                modifier = Modifier.width(120.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "UTH SmartTasks",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = UthTextBlue
            )
        }
    }
}

// Màn hình Onboarding hiển thị các trang giới thiệu
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(pages: List<OnboardingPage>, onGetStarted: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    if (pages.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No onboarding pages to display.")
        }
        return
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Các chấm hiển thị vị trí trang hiện tại
                Row {
                    repeat(pages.size) { index ->
                        val color = if (pagerState.currentPage == index) UthAppBlue else InactiveIndicatorColor
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }

                // Nút Skip
                if (pagerState.currentPage <= pages.size - 1) {
                    TextButton(onClick = onGetStarted) {
                        Text("skip", color = UthAppBlue, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        },

        // Thanh điều hướng dưới: nút Back và Next/Get Started
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Nút Back
                if (pagerState.currentPage > 0) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(UthAppBlue)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = BackButtonBackgroundColor)
                    }
                } else {
                    Spacer(modifier = Modifier.size(50.dp)) // Giữ layout ổn định
                }

                // Nút Next hoặc Get Started
                Button(
                    onClick = {
                        if (pagerState.currentPage < pages.size - 1) {
                            coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        } else {
                            onGetStarted()
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UthAppBlue),
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f)
                        .padding(start = if (pagerState.currentPage > 0) 16.dp else 0.dp)
                ) {
                    Text(
                        if (pagerState.currentPage < pages.size - 1) "Next" else "Get Started",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { pageIndex ->
            OnboardingPageContent(page = pages[pageIndex])
        }
    }
}

// Hiển thị nội dung của từng trang onboarding
@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ảnh minh họa
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.title,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .aspectRatio(1f)
                .padding(bottom = 24.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tiêu đề
        Text(
            page.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TitleTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Mô tả
        Text(
            page.description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = DescriptionTextColor,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

// ----- Preview cho màn hình Splash và Onboarding -----

@Preview(showBackground = true, name = "Splash Screen")
@Composable
fun SplashScreenPreview() {
    MaterialTheme {
        SplashScreen {}
    }
}

@Preview(showBackground = true, name = "Onboarding Screen (Phone)", device = Devices.PHONE)
@Composable
fun OnboardingScreenPreview() {
    val previewPages = listOf(
        OnboardingPage(R.drawable.bro, "Easy Time Management", "Description for easy time management preview."),
        OnboardingPage(R.drawable.bro2, "Increase Work Effectiveness", "Description for increasing work effectiveness preview."),
        OnboardingPage(R.drawable.bro3, "Reminder Notification", "Description for reminder notification preview.")
    )
    MaterialTheme {
        OnboardingScreen(pages = previewPages, onGetStarted = {})
    }
}
