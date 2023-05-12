package com.yusuftalhaklc.todoappwithtodoapi.models

data class UpdateResponse(
    val content: String,
    val id: Int,
    val is_completed: Boolean,
    val todolist_id: Int,
    val updated_at: String
)