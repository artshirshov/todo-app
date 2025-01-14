package com.artshirshov.todo.domain

import java.util.*

data class Tag(
    val id: UUID = UUID.randomUUID(),
    val name: String
)
