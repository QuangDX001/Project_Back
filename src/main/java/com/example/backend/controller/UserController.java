package com.example.backend.controller;


import com.example.backend.exception.ExceptionObject;
import com.example.backend.exception.StaffSelfDisableException;
import com.example.backend.model.User;
import com.example.backend.payload.dto.ProfileDTO;
import com.example.backend.payload.dto.UserDTO;
import com.example.backend.payload.dto.UserMapper;
import com.example.backend.payload.request.CreateUserRequest;
import com.example.backend.payload.request.UpdateUserRequest;
import com.example.backend.payload.response.MessageResponse;
import com.example.backend.payload.response.PageResponse;
import com.example.backend.security.config.AppConstants;
import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.security.service.UserDetailsImpl;
import com.example.backend.security.service.UserService;
import org.modelmapper.ModelMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {

        User useRequest = userService.createUser(createUserRequest);

        // convert entity to DTO
        UserDTO userResponse = modelMapper.map(useRequest, UserDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/updateUser/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UpdateUserRequest updateUserRequest) {

        User userRequest = userService.updateUser(id, updateUserRequest);

        UserDTO userResponse = modelMapper.map(userRequest, UserDTO.class);

        return ResponseEntity.ok().body(userResponse);

//        try{
//            long tokenId = getIdFromToken();
//            //User user = userService.getUserById(tokenId);
//            if (tokenId != id) {
//                throw new AccessDeniedException("Bạn không có quyền sửa tài khoản này");
//            } else {
//                userService.updateUser(id, updateUserRequest);
//                return new ResponseEntity<>("Chỉnh sửa tài khoản thành công.", HttpStatus.OK);
//            }
//        } catch (AccessDeniedException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
//        } catch (Exception e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok("Delete success");

        try {
            //User user = userService.getUserById(id);
            ExceptionObject exceptionObject = new ExceptionObject();
            Map<String, String> errorMap = new HashMap<>();
            int errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            exceptionObject.setCode(errorCode);
            long currentUser = getIdFromToken();
            if(id == currentUser){
                throw new StaffSelfDisableException("Can't delete yourself");
            }
            userService.deleteUser(id);
            return new ResponseEntity<>("Delete success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/id/{id}")
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable(name = "id") Long id) {
        try {
            //long idToken =  getIdFromToken();
            User user = userService.getUserById(id);
            //if (user.getId() == idToken){
            ProfileDTO dto = UserMapper.convertUserToProfile(user);
            return ResponseEntity.ok().body(dto);
//            } else {
//                return new ResponseEntity<>("It seems something goes wrong", HttpStatus.NOT_FOUND);
//            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getProfile/{username}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserByName(@PathVariable(name = "username") String username) {
        try {
            long id = getIdFromToken();
//            User u = userService.getUserById(id);
            User user = userService.getByUsername(username);
            if (user.getId() == id) {
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
}
