package com.yusuftalhaklc.todoappwithtodoapi.models

data class CreateListResponse(
    val completion_percentage: Double,
    val created_at: String,
    val id: Int,
    val title: String
)