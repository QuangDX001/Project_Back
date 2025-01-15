package com.example.backend.security.service.tasks.primaryTasks;

import com.example.backend.model.Account;
import com.example.backend.model.Task;
import com.example.backend.payload.dto.account.AccountByTaskIdDTO;
import com.example.backend.payload.dto.account.AccountDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskAddDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskUserAssignDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskAddDTO addTask(TaskAddDTO dto);

    TaskAddDTO addTaskForMod(TaskAddDTO dto);

    Task updateTask(Task task, String title);

    void deleteDoneTaskByUserId(Long userId);

    void deleteAllTasksByUserId(Long userId);                 

    void deleteTaskByIdAndUserId(Long id, Long userId);

    void changeStatusTask(Task task);

    void updateTaskPosition(List<TaskDTO> task);

    void assignTaskToUser(TaskUserAssignDTO dto);

    void withdrawTaskFromUser(Long taskId);

    AccountByTaskIdDTO getAccountByTaskId(Long taskId);

    List<Task> getTaskById(Long id);

    List<Task> getAllTasks();

    Page<Task> getTasksForMod(Pageable pageable);

    List<Task> getTaskAndSub(Long id);

}
