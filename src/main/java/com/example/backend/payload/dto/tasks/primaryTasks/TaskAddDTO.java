package com.example.backend.payload.dto.tasks.primaryTasks;

import com.example.backend.payload.dto.tasks.subTasks.SubTaskDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private List<SubTaskDTO> subTasks = new ArrayList<>();
}
