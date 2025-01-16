package com.artshirshov.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Sql(
        scripts = "/script/task-controller-test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
public class TaskControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAllTasks_shouldReturnTaskList() throws Exception {
        this.mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("Test Title1"));
    }

    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        this.mockMvc.perform(get("/api/v1/tasks/8a561a9a-b7c6-4cca-a4da-82b21ca591a4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("8a561a9a-b7c6-4cca-a4da-82b21ca591a4"))
                .andExpect(jsonPath("$.title").value("Test Title1"))
                .andExpect(jsonPath("$.description").value("Test Description1"))
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.tags[0]").value("Test Tag1"));
    }

    @Test
    void createTask_shouldCreateTask() throws Exception {
        String taskJson = """
                    {
                        "title": "New Task",
                        "description": "Test Description",
                        "tags": ["foo", "bar", "baz"]
                    }
                """;

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.tags.length()").value(3));
    }

    @Test
    void updateTask_shouldUpdateExistingTask() throws Exception {
        String updateJson = """
                    {
                        "title": "Updated Task",
                        "description": "Updated Description"
                    }
                """;

        mockMvc.perform(put("/api/v1/tasks/482b1119-ead5-4bf0-99df-6a2d6ff6847c")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    void toggleTaskStatus_shouldChangeTaskStatus() throws Exception {
        mockMvc.perform(patch("/api/v1/tasks/482b1119-ead5-4bf0-99df-6a2d6ff6847c/toggle-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Status changed"));

    }

    @Test
    void deleteTask_shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/fa36a8e2-2726-4bc5-8e89-a7e58388fa94"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }
}
