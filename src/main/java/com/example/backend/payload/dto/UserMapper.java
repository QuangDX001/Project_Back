package com.example.backend.payload.dto;


import com.example.backend.model.User;
import com.example.backend.security.service.UserDetailsImpl;

/**
 * Created by Admin on 10/13/2023
 */
public class UserMapper {

    public static UserDTO convertEntityToDTO(UserDetailsImpl user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static ProfileDTO convertUserToProfile(User user){
        ProfileDTO dto = new ProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
