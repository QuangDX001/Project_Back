package com.example.backend.controller;

import com.example.backend.dto.UserDto;
import com.example.backend.model.User;
import com.example.backend.payload.request.CreateUserRequest;
import com.example.backend.payload.request.UpdateUserRequest;
import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.security.service.UserService;
import org.modelmapper.ModelMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Admin on 10/10/2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/crud")
public class UserController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<UserDto> getAllUsers() {

        return userService.getAllUsers().stream().map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/asc")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<UserDto> getAllUsersAsc() {

        return userService.findAllOrderByNameAsc().stream().map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/desc")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<UserDto> getAllUsersDesc() {

        return userService.findAllOrderByNameDesc().stream().map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {

        User useRequest = userService.createUser(createUserRequest);

        // convert entity to DTO
        UserDto userResponse = modelMapper.map(useRequest, UserDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UpdateUserRequest updateUserRequest) {

        User userRequest = userService.updateUser(id, updateUserRequest);

        UserDto userResponse = modelMapper.map(userRequest, UserDto.class);

        return ResponseEntity.ok().body(userResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePost(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Delete success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.getUserById(id);

        // convert entity to DTO
        UserDto userResponse = modelMapper.map(user, UserDto.class);

        return ResponseEntity.ok().body(userResponse);
    }

    public int getIdFromToken() {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
        }
        int id = jwtUtils.getIdFromJwtToken(jwtToken);
        return id;
    }
}
