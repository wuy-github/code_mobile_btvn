package com.example.tuan_6_task_1.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.tuan_6_task_1.data.Attachment
import com.example.tuan_6_task_1.data.Subtask
import com.example.tuan_6_task_1.data.Task
import com.example.tuan_6_task_1.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(navController: NavController, viewModel: TaskViewModel, taskId: String) {
    LaunchedEffect(taskId) {
        viewModel.getTaskById(taskId)
    }

    val task = viewModel.selectedTask
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { /* Để trống vì không cần xử lý ảnh */ }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        } else {
            Toast.makeText(context, "Bạn đã từ chối quyền truy cập camera", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail", fontWeight = FontWeight.Bold, color = Color.Blue) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            cameraLauncher.launch(intent)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Photo")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (task != null) {
                TaskDetailContent(task = task)
            }
        }
    }
}

@Composable
private fun TaskDetailContent(task: Task, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Các thành phần UI được chia nhỏ
        TaskHeader(title = task.title, status = task.status, priority = task.priority)
        Divider()
        TaskDescription(description = task.description)

        if (task.subtasks.isNotEmpty()) {
            TaskSubtasks(subtasks = task.subtasks)
        }

        if (task.attachments.isNotEmpty()) {
            TaskAttachments(attachments = task.attachments)
        }
    }
}

@Composable
private fun TaskHeader(title: String, status: String, priority: String) {
    Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    Text("Status: $status - Priority: $priority", style = MaterialTheme.typography.titleSmall)
}

@Composable
private fun TaskDescription(description: String) {
    Text("Description", style = MaterialTheme.typography.titleMedium)
    Text(description, style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun TaskSubtasks(subtasks: List<Subtask>) {
    Spacer(Modifier.height(8.dp))
    Text("Subtasks", style = MaterialTheme.typography.titleMedium)
    subtasks.forEach { subtask ->
        Text("• ${subtask.title} (${if (subtask.isCompleted) "Done" else "Pending"})")
    }
}

@Composable
private fun TaskAttachments(attachments: List<Attachment>) {
    Spacer(Modifier.height(8.dp))
    Text("Attachments", style = MaterialTheme.typography.titleMedium)
    attachments.forEach { attachment ->
        Text("📄 ${attachment.fileName}")
    }
}