package com.example.backend.controller;


import com.example.backend.exception.ExceptionObject;
import com.example.backend.exception.StaffSelfDisableException;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.payload.dto.mapper.UserMapper;
import com.example.backend.payload.dto.user.*;
import com.example.backend.payload.request.UpdateUserRequest;
import com.example.backend.payload.response.PageResponse;
import com.example.backend.security.config.AppConstants;
import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.security.service.UserDetailsImpl;
import com.example.backend.security.service.users.EmailService;
import com.example.backend.security.service.users.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Admin on 10/10/2023
 */
@CrossOrigin(origins = "*", maxAge = 36000)
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

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {

        return userService.getAllUsers().stream().map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/asc")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<UserDTO> getAllUsersAsc() {

        return userService.findAllOrderByNameAsc().stream().map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/desc")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<UserDTO> getAllUsersDesc() {

        return userService.findAllOrderByNameDesc().stream().map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }


    @GetMapping("/paging")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public PageResponse getAllUsersByPage(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return userService.getPageUsers(pageNo, pageSize, sortBy, sortDir);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            long tokenId = getIdFromToken();
            User u = userService.getUserById(tokenId);
            boolean isAdmin = hasRoleWithId(u, 3);
            if (isAdmin || tokenId == id){
                userService.updateUser(id, updateUserRequest);
                return new ResponseEntity<>("Update user successfully.", HttpStatus.OK);
            } else {
                throw new AccessDeniedException("You don't have the right to update this account");
            }

        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/sendForgotPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String email){
        try{
            ExceptionObject exceptionObject = new ExceptionObject();
            Map<String, String> errorMap = new HashMap<>();
            int errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            exceptionObject.setCode(errorCode);
            if(userService.getUserByEmail(email) == null){
                errorMap.put("email", "This email doesn't exist");
                exceptionObject.setError(errorMap);
                return new ResponseEntity<>(exceptionObject, HttpStatus.BAD_REQUEST);
            } else {
                emailService.sendMail(email);
                return new ResponseEntity<>("Email Sent! Please check your email", HttpStatus.OK);
            }
        } catch (Exception e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changePassword(@PathVariable long id, @Valid @RequestBody ChangePassDTO dto) {
        try {
            long tokenId = getIdFromToken();
            ExceptionObject exceptionObject = new ExceptionObject();
            Map<String, String> errorMap = new HashMap<>();
            int errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            exceptionObject.setCode(errorCode);
            User existUser = userService.getUserById(id);
            if (tokenId != id) {
                errorMap.put("exception", "You don't have the right to change password of this account");
                exceptionObject.setError(errorMap);
                return new ResponseEntity<>(exceptionObject, HttpStatus.FORBIDDEN);
            } else if (!encoder.matches(dto.getPassword(), existUser.getPassword())) {
                errorMap.put("password", "Password is incorrect");
                exceptionObject.setError(errorMap);
                return new ResponseEntity<>(exceptionObject, HttpStatus.BAD_REQUEST);
            } else if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
                errorMap.put("confirmNewPassword", "Password not match, please confirm again");
                exceptionObject.setError(errorMap);
                return new ResponseEntity<>(exceptionObject, HttpStatus.BAD_REQUEST);
            } else {
                userService.resetPassword(existUser, dto.getNewPassword());
                return new ResponseEntity<>("Change password succesfully", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/changeEnableStatus/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeEnableStatus(@PathVariable(name = "id") Long id){
        try{
            User user = userService.getUserById(id);
            long currentAdmin = getIdFromToken();
            if(currentAdmin == id){
                throw new StaffSelfDisableException("You can not disable yourself");
            }
            User userRequest =userService.changeEnableStatus(user);

            StatusDTO userResponse = modelMapper.map(userRequest, StatusDTO.class);

            return ResponseEntity.ok().body(userResponse);
            //return new ResponseEntity<>("Change status successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/resetPasswordForAdmin/{username}")
    @PreAuthorize("hasRole('ADMIN')")       
    public ResponseEntity<?> resetPassForAdmin(@PathVariable("username") String username,
                                               @Valid @RequestBody ResetPasswordAdminDTO dto){
        try{
            userService.resetPasswordForAdmin(username, dto);
            return new ResponseEntity<>("Reset password for " + username + " successfully", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);    
        }
        
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(name = "id") Long id) {
        try {
            User user = userService.getUserById(id);

            StatusDTO userResponse = modelMapper.map(user, StatusDTO.class);
            return ResponseEntity.ok().body(userResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getProfile/{username}")
    public ResponseEntity<?> getUserByName(@PathVariable(name = "username") String username) {
        try {
            long id = getIdFromToken();
            User u = userService.getUserById(id);
            boolean isAdmin = hasRoleWithId(u, 3);
            User user = userService.getByUsername(username);
            if (isAdmin || user.getId() == id) {
                ProfileDTO dto = UserMapper.convertUserToProfile(user);
                return new ResponseEntity<>(dto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You don't have the rights to access this", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public long getIdFromToken() {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
        }
        return jwtUtils.getIdFromJwtToken(jwtToken);
    }

    public boolean hasRoleWithId(User user, int roleId){
        Set<Role> roles = user.getRoles();
        for(Role role : roles){
            if(role.getId() == roleId){
                return true;
            }
        }
        return false;
    }
}
