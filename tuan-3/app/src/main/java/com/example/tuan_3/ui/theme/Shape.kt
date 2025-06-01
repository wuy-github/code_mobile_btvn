package com.example.tuan_3.ui.theme // Đảm bảo đúng tên gói của bạn

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes // <-- Import Material3 Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)