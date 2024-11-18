package com.xml.yandextodo.data.model

import com.google.gson.annotations.SerializedName

data class TaskRequest(

	@SerializedName("element")
	val todoItem: TodoItemDto,

)