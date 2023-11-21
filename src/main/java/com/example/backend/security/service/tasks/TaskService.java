package com.example.backend.security.service.tasks;

import com.example.backend.model.Task;
import com.example.backend.payload.dto.task.TaskAddDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    //Page<Task> getTasksByStatusAndId(boolean status, Long userId, Pageable pageable);

    List<Task> getTasksByStatusAndId(boolean status, Long userId);

    TaskAddDTO addTask(TaskAddDTO dto);

    Task updateTask(Task task, String title);

    void deleteDoneTaskByUserId(Long userId);

    void deleteAllTasksByUserId(Long userId);

    void deleteTaskByIdAndUserId(Long id, Long userId);

    void changeStatusTask(Task task);

    //Page<Task> getTaskById(Long id, Pageable pageable);
    List<Task> getTaskById(Long id);

    //Page<Task> getAllTasks(Pageable pageable);

    List<Task> getAllTasks();

}
