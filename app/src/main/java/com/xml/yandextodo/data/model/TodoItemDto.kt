package com.xml.yandextodo.data.model

import com.google.gson.annotations.SerializedName

data class TodoItemDto(

	@SerializedName("last_updated_by")
	val lastUpdatedBy: String = "",

	@SerializedName("color")
	val color: String? = null,

	@SerializedName("importance")
	val importance: String,

	@SerializedName("created_at")
	val createdAt: Long,

	@SerializedName("files")
	val files: Any? = null,

	@SerializedName("changed_at")
	val changedAt: Long?,

	@SerializedName("id")
	val id: String,

	@SerializedName("text")
	val text: String,

	@SerializedName("deadline")
	val deadline: Long? = null,

	@SerializedName("done")
	val done: Boolean

)