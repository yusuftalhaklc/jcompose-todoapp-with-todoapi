package com.yusuftalhaklc.todoappwithtodoapi.models

data class UpdateBody(
    val content: String,
    val is_completed: Boolean
)