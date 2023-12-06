package com.example.backend.payload.dto.tasks.primaryTasks;

import com.example.backend.payload.dto.tasks.subTasks.SubTaskDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Admin on 11/2/2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private boolean isDone;
    private Long userId;
    private Integer position;
    private String username;
    private List<SubTaskDTO> subTasks;
}
