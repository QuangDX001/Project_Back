package com.example.backend.security.service.tasks;

import com.example.backend.model.Task;
import com.example.backend.payload.dto.task.TaskAddDTO;
import com.example.backend.payload.dto.task.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskAddDTO addTask(TaskAddDTO dto);

    Task updateTask(Task task, String title);

    void deleteDoneTaskByUserId(Long userId);

    void deleteAllTasksByUserId(Long userId);

    void deleteTaskByIdAndUserId(Long id, Long userId);

    void changeStatusTask(Task task);

    void updateTaskPosition(List<TaskDTO> task);

    List<Task> getTaskById(Long id);

    List<Task> getAllTasks();

}
