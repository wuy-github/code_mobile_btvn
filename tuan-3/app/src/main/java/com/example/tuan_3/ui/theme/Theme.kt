package com.example.tuan_3.ui.theme // Đảm bảo đúng tên gói của bạn

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme // <-- RẤT QUAN TRỌNG: Import MaterialTheme của Material 3
import androidx.compose.material3.darkColorScheme // <-- RẤT QUAN TRỌNG: Thay đổi từ darkColors sang darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme // <-- RẤT QUAN TRỌNG: Thay đổi từ lightColors sang lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat // Để điều khiển thanh trạng thái

// --- Định nghĩa các màu Material 3 ---
// Đảm bảo các màu này đã được định nghĩa trong ui.theme/Color.kt của bạn
// Nếu chưa, hãy tạo tệp Color.kt và thêm các màu này vào:
// package com.example.tuan_3.ui.theme
// import androidx.compose.ui.graphics.Color
// val Purple80 = Color(0xFFD0BCFF)
// val PurpleGrey80 = Color(0xFFCCC2DC)
// val Pink80 = Color(0xFFEFB8C8)
// val Purple40 = Color(0xFF6650a4)
// val PurpleGrey40 = Color(0xFF625b71)
// val Pink40 = Color(0xFF7D5260)
//
// Hoặc bạn có thể dùng các màu tự định nghĩa của mình:

private val DarkColorScheme = darkColorScheme( // <-- Dùng darkColorScheme
    primary = Color(0xFF1E88E5), // Xanh lam
    primaryContainer = Color(0xFF1565C0), // Thường là màu tối hơn của primary
    onPrimary = Color.White,
    secondary = Color(0xFFFFA000), // Cam/Nâu
    secondaryContainer = Color(0xFFFFCC80),
    onSecondary = Color.Black,
    tertiary = Color(0xFF03DAC5), // Có thể thêm màu thứ ba
    onTertiary = Color.Black,
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme( // <-- Dùng lightColorScheme
    primary = Color(0xFF1E88E5), // Xanh lam
    primaryContainer = Color(0xFF90CAF9),
    onPrimary = Color.White,
    secondary = Color(0xFFFFA000), // Cam/Nâu
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondary = Color.Black,
    tertiary = Color(0xFF03DAC5),
    onTertiary = Color.Black,
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun Tuan3Theme( // Sử dụng tên theme của bạn
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Dynamic color chỉ khả dụng trên Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Điều chỉnh màu thanh trạng thái (status bar)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme, // <-- RẤT QUAN TRỌNG: Dùng colorScheme thay vì colors
        typography = Typography, // Đảm bảo Typography được định nghĩa trong ui.theme/Type.kt
        shapes = Shapes, // Đảm bảo Shapes được định nghĩa trong ui.theme/Shape.kt
        content = content
    )
}