package com.example.backend.payload.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Admin on 10/23/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingDTO {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private boolean enable;
}
