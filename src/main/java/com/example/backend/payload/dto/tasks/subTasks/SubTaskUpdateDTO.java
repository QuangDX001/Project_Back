package com.example.backend.payload.dto.tasks.subTasks;

import com.example.backend.model.Task;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Admin on 11/30/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubTaskUpdateDTO {
    @NotBlank(message = "Title is required")
    private String title;

}
