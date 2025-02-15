package com.artshirshov.todo.service

import com.artshirshov.todo.dto.CreateTaskDto
import com.artshirshov.todo.dto.OperationDto
import com.artshirshov.todo.dto.TaskDto
import com.artshirshov.todo.dto.UpdateTaskDto
import com.artshirshov.todo.mapper.TagMapper
import com.artshirshov.todo.mapper.TaskMapper
import com.artshirshov.todo.repository.TaskRepository
import org.mapstruct.Builder
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val taskMapper: TaskMapper,
    private val tagMapper: TagMapper,
) {

    fun getAllTasks(): List<TaskDto> {
        return taskRepository.findAll()
            .map(taskMapper::toDto)
    }

    fun getTaskById(id: UUID): TaskDto? {
        val task = taskRepository.findById(id)
        return task?.let(taskMapper::toDto) ?: throw RuntimeException("Task with id $id does not exist")
    }

    fun createTask(request: CreateTaskDto): TaskDto {
        val task = taskMapper.toEntity(request)
        val tags = request.tags?.map(tagMapper::toEntity)

        val createdTask = taskRepository.create(task, tags)
        return taskMapper.toDto(createdTask)
    }

    fun updateTask(id: UUID, request: UpdateTaskDto): TaskDto? {
        val existingTask = getTaskByIdOrThrow(id)

        val updatedTask = existingTask.copy(
            title = request.title ?: existingTask.title,
            description = request.description ?: existingTask.description,
            done = request.done ?: existingTask.done,
            updatedAt = java.time.LocalDateTime.now()
        )

        val tags = request.tags?.map(tagMapper::toEntity)
        val task = taskRepository.update(updatedTask, tags)

        return taskMapper.toDto(task)
    }

    fun toggleTaskStatus(id: UUID): OperationDto {
        val task = getTaskByIdOrThrow(id)
        taskRepository.invertStatus(id, !task.done)
        return OperationDto("Status changed")
    }

    fun deleteTask(id: UUID): OperationDto {
        taskRepository.delete(id)
        return OperationDto("Deleted")
    }

    private fun getTaskByIdOrThrow(id: UUID) = (taskRepository.findById(id)
        ?: throw RuntimeException("Task with id $id does not exist"))
}
