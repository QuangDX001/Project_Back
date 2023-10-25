package com.example.backend.security.service;


import com.example.backend.model.User;
import com.example.backend.payload.request.CreateUserRequest;
import com.example.backend.payload.request.UpdateUserRequest;
import com.example.backend.payload.response.PageResponse;

import java.util.List;

/**
 * Created by Admin on 10/10/2023
 */

public interface UserService {
    List<User> getAllUsers();

    User createUser(CreateUserRequest createUserRequest);

    User updateUser(long id, UpdateUserRequest updateUserRequest);

    void deleteUser(long id);

    void resetPassword(User user,String newPassword);

    User getUserById(long id);

    List<User> findAllOrderByNameDesc();

    List<User> findAllOrderByNameAsc();

    User getByUsername(String username);

    PageResponse getPageUsers(int pageNo, int pageSize, String sortBy, String sortDir);
}
