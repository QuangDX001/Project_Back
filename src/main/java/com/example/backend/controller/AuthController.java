package com.example.backend.controller;


import com.example.backend.payload.dto.user.UserDTO;
import com.example.backend.payload.dto.mapper.UserMapper;
import com.example.backend.exception.ExceptionObject;
import com.example.backend.payload.request.LoginRequest;
import com.example.backend.payload.request.SignupRequest;
import com.example.backend.payload.response.JwtResponse;
import com.example.backend.repository.RoleRepository;
import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.security.service.UserDetailsImpl;
import com.example.backend.security.service.users.UserServiceImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Admin on 10/9/2023
 */

@CrossOrigin(origins = "*", maxAge = 36000)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            ExceptionObject exceptionObject = new ExceptionObject();
            Map<String, String> errorMap = new HashMap<>();
            int errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            exceptionObject.setCode(errorCode);
            if (userService.getByUsername(loginRequest.getUsername()) == null) {
                errorMap.put("username", "Username is not correct");
                exceptionObject.setError(errorMap);
                return new ResponseEntity<>(exceptionObject, HttpStatus.BAD_REQUEST);
            } else {
                String dbPassword = userService.getByUsername(loginRequest.getUsername()).getPassword();
                if (!encoder.matches(loginRequest.getPassword(), dbPassword)) {
                    errorMap.put("password", "Password is not correct");
                    exceptionObject.setError(errorMap);
                    return new ResponseEntity<>(exceptionObject, HttpStatus.BAD_REQUEST);
                } else {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwt = jwtUtils.generateJwtToken(authentication);

                    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                    UserDTO dto = UserMapper.convertEntityToDTO(userDetails);
                    List<String> roles = userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());
                    JwtResponse res = new JwtResponse(jwt, dto, roles);

                    return ResponseEntity.ok(res);
                }
            }
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            ExceptionObject exceptionObject = new ExceptionObject();
            Map<String, String> errorMap = new HashMap<>();
            int errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            exceptionObject.setCode(errorCode);
            if (userService.getByUsername(signupRequest.getUsername()) != null) {
                errorMap.put("username", "Username is already taken!");
                exceptionObject.setError(errorMap);
                return new ResponseEntity<>(exceptionObject, HttpStatus.BAD_REQUEST);
            } else {
                if (userService.getUserByEmail(signupRequest.getEmail()) != null) {
                    errorMap.put("email", "Email is already taken!");
                    exceptionObject.setError(errorMap);
                    return new ResponseEntity<>(exceptionObject, HttpStatus.BAD_REQUEST);
                } else {
                    //sign up
                    userService.signUp(signupRequest);
                    return new ResponseEntity<>("Signup successfully", HttpStatus.OK);
                }
            }
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
