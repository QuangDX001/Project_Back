package com.example.backend.payload.dto.mapper;


import com.example.backend.model.User;
import com.example.backend.payload.dto.user.ProfileDTO;
import com.example.backend.payload.dto.user.UserDTO;
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
        dto.setEnable((user.getEnable() == 1) ? true : false);
        return dto;
    }

    public static ProfileDTO convertUserToProfile(User user){
        ProfileDTO dto = new ProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAccount().getAddress());
        dto.setPhone(user.getAccount().getPhone());
        dto.setFirstName(user.getAccount().getFirstName());
        dto.setLastName(user.getAccount().getLastName());
        dto.setBalance(user.getAccount().getBalance());
        return dto;
    }
}
