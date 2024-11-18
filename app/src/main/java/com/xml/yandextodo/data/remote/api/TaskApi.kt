package com.xml.yandextodo.data.remote.api

import com.xml.yandextodo.data.model.TaskRequest
import com.xml.yandextodo.data.model.TaskResponse
import com.xml.yandextodo.data.model.TasksResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {
    @GET("list")
    suspend fun getTasks(): TasksResponse

    @GET("list/{id}")
    suspend fun getTask(@Path("id") id: String): TaskResponse

    @POST("list")
    suspend fun addTask(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: TaskRequest
    ): TaskResponse

    @PUT("list/{id}")
    suspend fun updateTask(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body task: TaskRequest
    ): TaskResponse

    @DELETE("list/{id}")
    suspend fun deleteTask(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int
    ): TaskResponse

}