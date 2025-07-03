package com.example.tuan_6_task_1.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Subtask(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

@JsonClass(generateAdapter = true)
data class Attachment(
    val id: Int,
    val fileName: String,
    val fileUrl: String
)

@JsonClass(generateAdapter = true)
data class Reminder(
    val id: Int,
    val time: String,
    val type: String
)

@JsonClass(generateAdapter = true)
data class Task(
    val id: Int,
    val title: String,
    @Json(name = "desImageURL")
    val descriptionImageUrl: String?,
    val description: String,
    val status: String,
    val priority: String,
    val category: String,
    val dueDate: String,
    val createdAt: String,
    val updatedAt: String,
    val subtasks: List<Subtask>,
    val attachments: List<Attachment>,
    val reminders: List<Reminder>
)


@JsonClass(generateAdapter = true)
data class ApiDetailResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: Task
)

@JsonClass(generateAdapter = true)
data class ApiResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: List<Task>
)
