package com.example.tuan_6_task_1.network

import com.example.tuan_6_task_1.data.ApiDetailResponse
import com.example.tuan_6_task_1.data.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path


interface TaskApiService {
    @GET("tasks")
    suspend fun getTasks(): ApiResponse

    @GET("task/{id}")
    suspend fun getTaskById(@Path("id") taskId: String): ApiDetailResponse

    @DELETE("task/{id}")
    suspend fun deleteTask(@Path("id") taskId: String)
}