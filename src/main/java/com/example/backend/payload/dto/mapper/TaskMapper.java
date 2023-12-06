package com.example.backend.payload.dto.mapper;

import com.example.backend.model.Task;
import com.example.backend.payload.dto.tasks.subTasks.SubTaskDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskDTO;

import java.util.List;
import java.util.stream.Collectors;

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
        dto.setPosition(task.getPosition());
        dto.setUsername(task.getUser().getUsername());

        // Convert subtasks to SubtaskDTO
        List<SubTaskDTO> subtaskDTOs = task.getSubTasks().stream()
                .map(subtask -> {
                    SubTaskDTO subtaskDTO = new SubTaskDTO();
                    subtaskDTO.setId(subtask.getId());
                    subtaskDTO.setTitle(subtask.getTitle());
                    subtaskDTO.setPosition(subtask.getPosition());
                    subtaskDTO.setDone(subtask.isDone());
                    subtaskDTO.setPrimaryTask(subtask.getTask().getId());
                    return subtaskDTO;
                })
                .collect(Collectors.toList());

        dto.setSubTasks(subtaskDTOs);

        return dto;
    }
}
