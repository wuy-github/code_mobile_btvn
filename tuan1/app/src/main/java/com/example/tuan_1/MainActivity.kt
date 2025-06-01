package com.example.tuan_1
import androidx.compose.ui.draw.shadow
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan_1.ui.theme.Tuan1Theme

// xet giao dien nguoi dung
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tuan1Theme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize()) {

        IconButton(
            onClick = { /* TODO: Handle back */ },
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Back")
        }

        IconButton(
            onClick = { /* TODO: Handle edit */ },
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_edit), contentDescription = "Edit")
        }

        // Nội dung chính
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .shadow(8.dp, shape = CircleShape)

            )


            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Dang Quoc Huy",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ho Chi Minh, VN",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    }
}
