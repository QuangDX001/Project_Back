package com.example.backend.security.service.users;


import com.example.backend.model.User;
import com.example.backend.payload.dto.user.ResetPasswordAdminDTO;
import com.example.backend.payload.request.SignupRequest;
import com.example.backend.payload.request.UpdateUserRequest;
import com.example.backend.payload.response.PageResponse;

import java.util.List;

/**
 * Created by Admin on 10/10/2023
 */

public interface UserService {
    List<User> getAllUsers();

    User signUp(SignupRequest signupRequest);

    void updateUser(long id, UpdateUserRequest updateUserRequest);

    void resetPassword(User user,String newPassword);

    void resetPasswordForAdmin(String username, ResetPasswordAdminDTO dto);

    User getUserByEmail(String email);

    User getUserById(long id);

    List<User> findAllOrderByNameDesc();

    List<User> findAllOrderByNameAsc();

    User getByUsername(String username);

    void changeEnableStatus(User user);

    PageResponse getPageUsers(int pageNo, int pageSize, String sortBy, String sortDir);
}
