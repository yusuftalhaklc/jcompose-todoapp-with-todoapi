package com.yusuftalhaklc.todoappwithtodoapi.models

data class ListResponseItem(
    val completion_percentage: Double,
    val id: Int,
    val title: String,
    val todo_items: List<TodoItem>
)