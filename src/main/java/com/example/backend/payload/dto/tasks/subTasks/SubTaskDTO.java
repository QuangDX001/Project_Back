package com.example.backend.payload.dto.tasks.subTasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Admin on 11/29/2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubTaskDTO {
    private Long id;
    private String title;
    private boolean isDone;
    private Integer position;
    private Long primaryTask;
}
