package com.example.backend.security.service;

import com.example.backend.exception.DuplicateRecordException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.ERole;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.payload.dto.PagingDTO;
import com.example.backend.payload.dto.UserDTO;
import com.example.backend.payload.request.CreateUserRequest;
import com.example.backend.payload.request.UpdateUserRequest;
import com.example.backend.payload.response.PageResponse;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Admin on 10/10/2023
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new DuplicateRecordException("New email already exists in the system");
        }
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new DuplicateRecordException("New email already exists in the system");
        }
        //create user
        User user = new User(createUserRequest.getUsername(),
                createUserRequest.getEmail(),
                encoder.encode(createUserRequest.getPassword()));
        Set<String> strRoles = createUserRequest.getRole();
        Set<Role> roles;
        roles = validatedRoles(strRoles);
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(long id, UpdateUserRequest updateUserRequest) {
        return null;
    }

    @Override
    public void deleteUser(long id) {
        User result = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found"));
        userRepository.delete(result);
    }

    @Override
    public void resetPassword(User user, String newPassword) {
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("No user found");
        }
    }

    @Override
    public List<User> findAllOrderByNameDesc() {
        return userRepository.findAllOrderByNameDesc();
    }

    @Override
    public List<User> findAllOrderByNameAsc() {
        return userRepository.findAllOrderByNameAsc();
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> account = userRepository.findByUsername(username);
        if (account.isPresent()) {
            User user = account.get();
            return user;
        } else {
            return null;
        }
    }

    @Override
    public PageResponse getPageUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<User> users = userRepository.findAll(pageable);

        // get content for page object
        List<User> listOfPosts = users.getContent();

        List<PagingDTO> content = listOfPosts.stream().map(user -> modelMapper.map(user, PagingDTO.class)).collect(Collectors.toList());

        PageResponse pageResponse = new PageResponse();
        pageResponse.setContent(content);
        pageResponse.setPageNo(users.getNumber());
        pageResponse.setPageSize(users.getSize());
        pageResponse.setTotalElements(users.getTotalElements());
        pageResponse.setTotalPages(users.getTotalPages());
        pageResponse.setLast(users.isLast());

        return pageResponse;
    }


    public Set<Role> validatedRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        return roles;
    }
}
