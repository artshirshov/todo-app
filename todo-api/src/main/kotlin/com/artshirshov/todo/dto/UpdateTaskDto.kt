package com.artshirshov.todo.dto

data class UpdateTaskDto(
    val title: String?,
    val description: String?,
    val done: Boolean?,
    val tags: List<String>?
)
