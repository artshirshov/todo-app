package com.artshirshov.todo;

import com.artshirshov.todo.domain.Tag;
import com.artshirshov.todo.domain.Task;
import com.artshirshov.todo.dto.CreateTaskDto;
import com.artshirshov.todo.dto.OperationDto;
import com.artshirshov.todo.dto.TaskDto;
import com.artshirshov.todo.dto.UpdateTaskDto;
import com.artshirshov.todo.mapper.TagMapper;
import com.artshirshov.todo.mapper.TaskMapper;
import com.artshirshov.todo.repository.TaskRepository;
import com.artshirshov.todo.service.TaskService;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Тесты сервиса по работе с задачами")
class TaskServiceTest {
    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final TaskMapper taskMapper = mock(TaskMapper.class);
    private final TagMapper tagMapper = mock(TagMapper.class);

    private TaskService taskService = new TaskService(taskRepository, taskMapper, tagMapper);

    private UUID id;
    private TaskDto taskDto;
    private Tag tag;
    private Task task;
    private CreateTaskDto createTaskDto;
    private UpdateTaskDto updateTaskDto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        tag = new Tag(id, "Test Tag");
        taskDto = new TaskDto(id, "Test TaskDto", "Description", false, Collections.singletonList("Test Tag"));
        task = new Task(id, "Test Task", "Description", false, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet());
        createTaskDto = new CreateTaskDto("Test Task", "Description", Collections.singletonList("Test Tag"));
        updateTaskDto = new UpdateTaskDto("Updated Task", "Updated Description", true, null);
    }

    @Test
    void getAllTasks_shouldReturnTaskList() {
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));
        when(taskMapper.toDto(any())).thenReturn(taskDto);

        List<TaskDto> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals(taskDto, result.getFirst());
        verify(taskRepository).findAll();
    }

    @Test
    void getTaskById_shouldReturnTask() {
        when(taskRepository.findById(id)).thenReturn(task);
        when(taskMapper.toDto(any())).thenReturn(taskDto);

        TaskDto result = taskService.getTaskById(id);

        assertNotNull(result);
        assertEquals(taskDto, result);
        verify(taskRepository).findById(id);
    }

    @Test
    void getTaskById_shouldThrowExceptionIfNotFound() {
        when(taskRepository.findById(id)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.getTaskById(id));
        assertEquals("Task with id " + id + " does not exist", exception.getMessage());
    }

    @Test
    void createTask_shouldCreateAndReturnTask() {
        when(taskMapper.toEntity(createTaskDto)).thenReturn(task);
        when(taskRepository.create(any(), any())).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.createTask(createTaskDto);

        assertNotNull(result);
        assertEquals(taskDto, result);
        verify(taskRepository).create(any(), any());
    }

    @Test
    void updateTask_shouldUpdateAndReturnTask() {
        when(taskRepository.findById(id)).thenReturn(task);
        when(taskMapper.toDto(any())).thenReturn(taskDto);

        TaskDto result = taskService.updateTask(id, updateTaskDto);

        assertNotNull(result);
        assertEquals(taskDto, result);
        verify(taskRepository).update(any(), any());
    }

    @Test
    void toggleTaskStatus_shouldToggleStatus() {
        when(taskRepository.findById(id)).thenReturn(task);

        OperationDto result = taskService.toggleTaskStatus(id);

        assertNotNull(result);
        assertEquals("Status changed", result.getMessage());
        verify(taskRepository).invertStatus(id, !taskDto.getDone());
    }

    @Test
    void deleteTask_shouldDeleteTask() {
        OperationDto result = taskService.deleteTask(id);

        assertNotNull(result);
        assertEquals("Deleted", result.getMessage());
        verify(taskRepository).delete(id);
    }
}
