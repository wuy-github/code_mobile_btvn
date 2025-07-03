package com.example.tuan_5 // Giữ nguyên package của bạn

import android.R.attr.fontWeight
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tuan_5.ui.theme.Tuan_5Theme

// ================================= MODEL =================================
data class Task(val id: Long = System.currentTimeMillis(), val title: String, val description: String)

// ============================== VIEWMODEL ===============================
// ViewModel để quản lý cả danh sách Task
class TaskViewModel : ViewModel() {
    // ---- Trạng thái cho màn hình Thêm mới ----
    private val _taskTitle = mutableStateOf("")
    val taskTitle: State<String> = _taskTitle

    private val _taskDescription = mutableStateOf("")
    val taskDescription: State<String> = _taskDescription

    // ---- Trạng thái cho danh sách Task ----
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> = _tasks

    init {
        // Thêm một vài task mẫu
        _tasks.add(Task(title = "Complete Android Project", description = "Finish the UI, integrate API, and write documentation"))
        _tasks.add(Task(title = "Learn Jetpack Compose", description = "Go through the official documentation and codelabs"))
    }


    fun onTitleChange(newTitle: String) {
        _taskTitle.value = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        _taskDescription.value = newDescription
    }

    fun addTask() {
        if (taskTitle.value.isNotBlank() && taskDescription.value.isNotBlank()) {
            val newTask = Task(title = taskTitle.value, description = taskDescription.value)
            _tasks.add(0, newTask) // Thêm vào đầu danh sách
            Log.d("TaskViewModel", "Task added: $newTask")

            // Xoá trống các ô nhập liệu
            _taskTitle.value = ""
            _taskDescription.value = ""
        }
    }
}

// ================================ VIEW ==================================

// Màn hình 1: Danh sách các Task
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, taskViewModel: TaskViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center ) {
                        Text("List",fontWeight = FontWeight.Bold, fontSize = 30.sp)

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFFFFF),
                    titleContentColor = Color(0xFF2D8CC9),

                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Di chuyển tới màn hình thêm mới
                navController.navigate("add_task_screen")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        // LazyColumn chỉ render những item đang hiển thị trên màn hình, rất hiệu quả
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {

            items(taskViewModel.tasks, key = { it.id }) { task ->
                val randomColor = listOf(
                    Color(0xFFE3F2FD),
                    Color(0xFFFFF9C4),
                    Color(0xFFFFEBEE),
                    Color(0xFFC8E6C9),
                    Color(0xFFFFF3E0)
                ).random()
                TaskItem(task = task, backgroundColor = randomColor)
            }
        }
    }
}

// Một item trong danh sách
@Composable
fun TaskItem(task: Task ,backgroundColor: Color = Color(0xFFE3F2FD)) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor =backgroundColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = task.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}


// Màn hình 2: Thêm Task mới
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTaskScreen(navController: NavController, taskViewModel: TaskViewModel) {
    val title by taskViewModel.taskTitle
    val description by taskViewModel.taskDescription
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center // Căn giữa nội dung của Box
                    )
                    { Text("Add New Task") }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Quay lại màn hình trước
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF6200EE),
                    navigationIconContentColor = Color.Black
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { taskViewModel.onTitleChange(it) },
                label = { Text("Task") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = description,
                onValueChange = { taskViewModel.onDescriptionChange(it) },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        taskViewModel.addTask()
                        Toast.makeText(context, "Task Added!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack() // Tự động quay về màn hình danh sách
                    } else {
                        Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Add")
            }
        }
    }
}

// ========================== ACTIVITY CHÍNH & NAVIGATION ==============================
class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tuan_5Theme {
                // Thiết lập hệ thống điều hướng
                AppNavigation(taskViewModel = taskViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(taskViewModel: TaskViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "task_list_screen") {
        // Route cho màn hình danh sách
        composable("task_list_screen") {
            TaskListScreen(navController = navController, taskViewModel = taskViewModel)
        }
        // Route cho màn hình thêm mới
        composable("add_task_screen") {
            AddNewTaskScreen(navController = navController, taskViewModel = taskViewModel)
        }
    }
}


// ============================== PREVIEW =================================
@Preview(showBackground = true)
@Composable
fun TaskListScreenPreview() {
    Tuan_5Theme {
        // Preview không thể điều hướng, nên ta truyền NavController giả
        val navController = rememberNavController()
        TaskListScreen(navController = navController, taskViewModel = TaskViewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun AddNewTaskScreenPreview() {
    Tuan_5Theme {
        val navController = rememberNavController()
        AddNewTaskScreen(navController = navController, taskViewModel = TaskViewModel())
    }
}