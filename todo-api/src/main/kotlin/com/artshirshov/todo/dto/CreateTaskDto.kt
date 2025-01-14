package com.artshirshov.todo.dto

data class CreateTaskDto(
    val title: String,
    val description: String?,
    val tags: List<String>?
)
