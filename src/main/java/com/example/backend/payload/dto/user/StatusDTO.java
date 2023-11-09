package com.example.backend.payload.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Admin on 11/9/2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDTO {
    private Long id;
    private boolean enable;
    private List<String> roles;
}
