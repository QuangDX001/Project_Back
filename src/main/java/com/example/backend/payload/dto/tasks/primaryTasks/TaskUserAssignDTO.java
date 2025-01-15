package com.example.backend.payload.dto.tasks.primaryTasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Admin on 3/25/2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUserAssignDTO {
    //private List<Long> userIds; // Assuming user ID is of type Long
    private Long userId;
    private Long taskId;
}
