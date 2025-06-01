package com.example.tuan2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan2.ui.theme.Tuan2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { // Hoặc MaterialTheme
                AgeCheckerScreen()
            }
        }
    }
}

@Composable
fun AgeCheckerScreen() {
    var hoTen by remember { mutableStateOf("") }
    var tuoiInput by remember { mutableStateOf("") }
    var ketQua by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "THỰC HÀNH 01",
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0), shape = MaterialTheme.shapes.medium) // Màu nền xám nhạt
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = hoTen,
                onValueChange = { hoTen = it },
                label = { Text("Họ và tên") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = tuoiInput,
                onValueChange = { tuoiInput = it.filter { char -> char.isDigit() } }, // Chỉ cho phép nhập số
                label = { Text("Tuổi") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (hoTen.isBlank() || tuoiInput.isBlank()) {
                        ketQua = "Vui lòng nhập đầy đủ họ tên và tuổi."
                    } else {
                        val tuoi = tuoiInput.toIntOrNull()
                        if (tuoi == null) {
                            ketQua = "Tuổi không hợp lệ. Vui lòng nhập số."
                        } else {
                            val ten = hoTen.trim()
                            val phanLoai = when {
<<<<<<< HEAD
                                tuoi < 0 -> "Tuổi không thể là số âm."
                                tuoi < 2 -> "Em bé"
                                tuoi <= 5 -> "Trẻ em"
                                tuoi <= 65 -> "Người lớn"
                                else -> "Người già"
=======
                                tuoi < 0 -> "Tuổi không thể là số âm." // Thêm kiểm tra tuổi âm
                                tuoi < 2 -> "Em bé"  // Giả định: <2 tuổi
                                tuoi <= 5 -> "Trẻ em" // Giả định: 2-5 tuổi (thay vì 2-6 để không trùng)
                                tuoi <= 65 -> "Người lớn" // Giả định: 6-65 tuổi
                                else -> "Người già" // >65 tuổi
>>>>>>> ca0061f71161afc6ea10f4ac02f4e152e3010d61
                            }
                            if (tuoi < 0) {
                                ketQua = phanLoai
                            } else {
                                ketQua = "Chào $ten!\nBạn là: $phanLoai."
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)) // Màu xanh dương cho nút
            ) {
                Text("Kiểm tra", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (ketQua != null) {
            Text(
                text = ketQua!!,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp),
                color = if (ketQua!!.contains("Vui lòng") || ketQua!!.contains("không hợp lệ") || ketQua!!.contains("không thể là số âm")) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true, name = "Age Checker Preview", device = "spec:width=360dp,height=640dp,dpi=480")
@Composable
fun AgeCheckerScreenPreview() {
<<<<<<< HEAD
    MaterialTheme {
=======
    MaterialTheme { // Hoặc MaterialTheme
>>>>>>> ca0061f71161afc6ea10f4ac02f4e152e3010d61
        AgeCheckerScreen()
    }
}