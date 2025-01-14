package com.artshirshov.todo.dto

import java.util.*

data class TaskDto(
    val id: UUID,
    val title: String,
    val description: String?,
    val done: Boolean,
    var tags: List<String> = emptyList()
)
