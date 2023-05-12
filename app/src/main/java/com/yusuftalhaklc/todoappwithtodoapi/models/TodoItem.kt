package com.yusuftalhaklc.todoappwithtodoapi.models

data class TodoItem(
    val content: String,
    val created_at: String,
    val deleted_at: String,
    val id: Int,
    val is_completed: Boolean,
    val todolist_id: Int,
    val updated_at: String
)