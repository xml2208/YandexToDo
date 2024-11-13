package com.xml.yandextodo.data.model

import com.google.gson.annotations.SerializedName

data class TasksResponse(

	@SerializedName("list")
	val list: List<TodoItemDto>,

	@SerializedName("revision")
	val revision: Int,

	@SerializedName("status")
	val status: String? = null
)