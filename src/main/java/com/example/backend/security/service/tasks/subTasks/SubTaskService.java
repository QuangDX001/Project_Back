package com.example.backend.security.service.tasks.subTasks;

import com.example.backend.model.SubTask;
import com.example.backend.payload.dto.tasks.subTasks.ChangeStatusDTO;
import com.example.backend.payload.dto.tasks.subTasks.SubTaskAddDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskDTO;

import java.util.List;

/**
 * Created by Admin on 11/29/2023
 */
public interface SubTaskService {

    SubTaskAddDTO addTask(SubTaskAddDTO dto);

    SubTask updateTask(SubTask task, String title);

    Long deleteTaskById(Long id);

    ChangeStatusDTO changeStatusTask(SubTask task);

    void updateTaskPosition(List<TaskDTO> task);
}
