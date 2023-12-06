package com.example.backend.payload.dto.tasks.subTasks;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Admin on 11/29/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubTaskAddDTO {
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    private Integer position;
    private Long taskId;
}
