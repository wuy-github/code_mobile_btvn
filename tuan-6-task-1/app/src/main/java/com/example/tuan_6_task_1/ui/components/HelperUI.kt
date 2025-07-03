package com.example.tuan_6_task_1.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun EmptyView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("No Tasks Yet", style = MaterialTheme.typography.headlineSmall)
        Text("Add a new task to get started!", style = MaterialTheme.typography.bodyMedium)
    }
}

fun generateRandomColor(): Color {
    return Color(
        red = Random.nextFloat() * 0.7f + 0.3f,
        green = Random.nextFloat() * 0.7f + 0.3f,
        blue = Random.nextFloat() * 0.7f + 0.3f,
    )
}