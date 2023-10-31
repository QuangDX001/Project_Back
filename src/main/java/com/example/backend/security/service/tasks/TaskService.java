package com.example.backend.security.service.tasks;

import com.example.backend.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getTasksByStatus(boolean status);

    void deleteTasksByStatus(boolean status);

}
