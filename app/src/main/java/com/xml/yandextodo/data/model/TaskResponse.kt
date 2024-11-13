package com.xml.yandextodo.data.model

import com.google.gson.annotations.SerializedName

data class TaskResponse(

	@SerializedName("element")
	val element: TodoItemDto,

	@SerializedName("revision")
	val revision: Int,

	@SerializedName("status")
	val status: String? = null
)