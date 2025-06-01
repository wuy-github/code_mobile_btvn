package com.example.tuan_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
// Import phiên bản icon mới nhất
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tuan_3.ui.theme.Tuan3Theme
import androidx.compose.foundation.layout.FlowRow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tuan3Theme {
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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController = navController) }
        composable("componentsList") { ComponentsListScreen(navController = navController) }
        composable("textDetail") { TextDetailScreen(navController = navController) }
        composable("imageDetail") { ImageDetailScreen(navController = navController) }
        composable("textFieldDetail") { TextFieldDetailScreen(navController = navController) }
        composable("rowLayout") { RowLayoutScreen(navController = navController) }
        composable("columnLayout") { ColumnLayoutScreen(navController = navController) }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "Đặng Quốc Huy", style = MaterialTheme.typography.headlineSmall)
            Text(text = "2251120156", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Jetpack Compose Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Jetpack Compose",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
                color = Color.DarkGray
            )
        }
        Button(
            onClick = { navController.navigate("componentsList") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White
            )
        ) {
            Text(text = "I'm ready", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentsListScreen(navController: NavController) {
    val lightBlueBg = Color(0xFFD0E4FF)
    val lightRedBg = Color(0xFFFADBD8)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UI Components List", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "back icon", tint = Color(0xFF0D47A1))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        val components = remember {
            listOf(
                ComponentGroup("Display", listOf(ComponentItem("Text", "Displays text"), ComponentItem("Image", "Displays an image"))),
                ComponentGroup("Input", listOf(ComponentItem("TextField", "Input field for text"), ComponentItem("PasswordField", "Input field for passwords"))),
                ComponentGroup("Layout", listOf(ComponentItem("Column", "Arranges elements vertically"), ComponentItem("Row", "Arranges elements horizontally")))
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            components.forEach { group ->
                item {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                }
                items(group.items) { item ->
                    ComponentListItem(item = item, cardColor = lightBlueBg) {
                        when (item.name) {
                            "Text" -> navController.navigate("textDetail")
                            "Image" -> navController.navigate("imageDetail")
                            "TextField" -> navController.navigate("textFieldDetail")
                            "Row" -> navController.navigate("rowLayout")
                            "Column" -> navController.navigate("columnLayout")
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = lightRedBg)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tự tìm hiểu", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Tìm ra tất cả các thành phần UI Cơ bản", style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}

@Composable
fun ComponentListItem(item: ComponentItem, cardColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.description, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDetailScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Text Detail", color = Color(0xFF0D47A1), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "back icon", tint = Color(0xFF0D47A1))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .wrapContentSize(Alignment.Center)
                .padding(horizontal = 16.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("The ", color = Color.Black, fontSize = 32.sp)
                Text("quick ", color = Color.Black, fontSize = 32.sp)
                Text("Brown ", color = Color(0xFFFFA000), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text("fox ", color = Color.Black, fontSize = 32.sp)
                Text("jumps ", color = Color.Black, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text("over ", color = Color.Black, fontSize = 32.sp)
                Text("the ", color = Color.Black, fontSize = 32.sp)
                Text("lazy ", color = Color.Black, fontSize = 32.sp)
                Text("dog.", color = Color.Black, fontSize = 32.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Images", color = Color(0xFF0D47A1), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "back icon", tint = Color(0xFF0D47A1))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.truong1),
                    contentDescription = "Image 1",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("https://giaothongvantaitphcm.edu.vn/wp-content/uploads/2025/01/Logo-GTVT.png", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.truong2),
                    contentDescription = "Image 2",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("In app", fontSize = 14.sp, color = Color.DarkGray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldDetailScreen(navController: NavController) {
    var textValue by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TextField", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "back icon", tint = Color(0xFF0D47A1))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = textValue,
                onValueChange = { newValue -> textValue = newValue },
                label = { Text("Thông tin nhập") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = textValue.ifEmpty { "Tự động cập nhật dữ liệu theo textfield" },
                color = Color.Red,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowLayoutScreen(navController: NavController) {
    val lightBlue = Color(0xFFD6EAF8)
    val darkBlue = Color(0xFF2E86C1)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Row Layout", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "back icon", tint = Color(0xFF0D47A1))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    repeat(4) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(60.dp).background(lightBlue, RoundedCornerShape(8.dp)))
                            Box(modifier = Modifier.size(width = 80.dp, height = 60.dp).background(darkBlue, RoundedCornerShape(8.dp)))
                            Box(modifier = Modifier.size(60.dp).background(lightBlue, RoundedCornerShape(8.dp)))
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnLayoutScreen(navController: NavController) {
    val lightGreen = Color(0xFFD5F5E3)
    val darkGreen = Color(0xFF2ECC71)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Column Layout", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "back icon", tint = Color(0xFF0D47A1))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(80.dp).background(lightGreen, RoundedCornerShape(12.dp)))
                    Box(modifier = Modifier.fillMaxWidth().height(80.dp).background(darkGreen, RoundedCornerShape(12.dp)))
                    Box(modifier = Modifier.fillMaxWidth().height(80.dp).background(lightGreen, RoundedCornerShape(12.dp)))
                }
            }
        }
    }
}

data class ComponentGroup(val name: String, val items: List<ComponentItem>)
data class ComponentItem(val name: String, val description: String)