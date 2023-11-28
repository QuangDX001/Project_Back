package com.example.backend.payload.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Admin on 11/2/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAddDTO {
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    private Integer position;
    private Long userId;
}
