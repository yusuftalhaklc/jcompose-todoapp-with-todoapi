package com.yusuftalhaklc.todoappwithtodoapi.models

data class ContentResponse(
    val content: String,
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val is_completed: Boolean,
    val todolist_id: Int,
    val updated_at: Any
)