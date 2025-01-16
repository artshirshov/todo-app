package com.artshirshov.todo.controller

import com.artshirshov.todo.dto.CreateTaskDto
import com.artshirshov.todo.dto.OperationDto
import com.artshirshov.todo.dto.TaskDto
import com.artshirshov.todo.dto.UpdateTaskDto
import com.artshirshov.todo.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/tasks")
@CrossOrigin(origins = ["http://localhost:5173"])
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getAllTasks(): List<TaskDto> =
        taskService.getAllTasks()

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: UUID): TaskDto? =
        taskService.getTaskById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(@RequestBody request: CreateTaskDto): TaskDto =
        taskService.createTask(request)

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: UUID, @RequestBody request: UpdateTaskDto): TaskDto? =
        taskService.updateTask(id, request)

    @PatchMapping("/{id}/toggle-status")
    fun toggleTaskStatus(@PathVariable id: UUID): OperationDto =
        taskService.toggleTaskStatus(id)

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: UUID): OperationDto =
        taskService.deleteTask(id)
}
