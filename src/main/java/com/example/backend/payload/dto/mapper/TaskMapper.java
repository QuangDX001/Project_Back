package com.example.backend.payload.dto.mapper;

import com.example.backend.model.Task;
import com.example.backend.payload.dto.task.TaskDTO;

/**
 * Created by Admin on 11/2/2023
 */
public class TaskMapper {
    public static TaskDTO convertEntityToDTO(Task task){
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setUserId(task.getUser().getId());
        dto.setTitle(task.getTitle());
        dto.setDone((task.isDone()) ? true : false);
        return dto;
    }
}
