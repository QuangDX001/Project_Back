package com.example.backend.payload.dto.tasks.subTasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Admin on 12/5/2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusDTO {
    private String message;
    private Long primaryTask;

    // getters, setters, and constructors
}