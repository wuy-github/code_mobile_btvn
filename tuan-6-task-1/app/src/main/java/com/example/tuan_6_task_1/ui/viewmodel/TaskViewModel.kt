package com.example.tuan_6_task_1.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuan_6_task_1.data.Task
import com.example.tuan_6_task_1.network.RetrofitInstance // Import RetrofitInstance
import com.example.tuan_6_task_1.network.TaskApiService    // Import Service
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    var taskList by mutableStateOf<List<Task>>(emptyList())
        private set
    var selectedTask by mutableStateOf<Task?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Gọi apiService một cách gọn gàng
    private val apiService: TaskApiService = RetrofitInstance.api

    fun getTasks() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                taskList = apiService.getTasks().data
            } catch (e: Exception) {
                errorMessage = "Không thể tải danh sách công việc: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun getTaskById(taskId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            selectedTask = null
            try {
                selectedTask = apiService.getTaskById(taskId).data
            } catch (e: Exception) {
                errorMessage = "Không thể tải chi tiết công việc: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            errorMessage = null
            try {
                apiService.deleteTask(taskId)
                taskList = taskList.filterNot { it.id.toString() == taskId }
            } catch (e: Exception) {
                errorMessage = "Xóa công việc thất bại: ${e.message}"
            }
        }
    }
}