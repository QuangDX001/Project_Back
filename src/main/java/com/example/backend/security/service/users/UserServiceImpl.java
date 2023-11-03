package com.example.backend.security.service.users;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.ERole;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.payload.dto.user.PagingDTO;
import com.example.backend.payload.dto.user.ResetPasswordAdminDTO;
import com.example.backend.payload.request.SignupRequest;
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
public class UserServiceImpl implements UserService  {

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
    public User signUp(SignupRequest signupRequest) {
        User user = convertSignupRequestToUser(signupRequest);
        return userRepository.save(user);
    }

    @Override
    public void updateUser(long id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.getReferenceById(id);
        user.getAccount().setFirstName(updateUserRequest.getFirstName());
        user.getAccount().setLastName(updateUserRequest.getLastName());
        user.getAccount().setAddress(updateUserRequest.getAddress());
        user.getAccount().setPhone(updateUserRequest.getPhone());
        userRepository.save(user);
    }

    @Override
    public void resetPassword(User user, String newPassword) {
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void resetPasswordForAdmin(String username, ResetPasswordAdminDTO dto) {
        User user = getByUsername(username);
        user.setPassword(encoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
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
    public void changeEnableStatus(User user) {
        if(user.getEnable() == 1){
            user.setEnable((byte) 0);
        } else {
            user.setEnable((byte) 1);
        }
        userRepository.save(user);
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

    private User convertSignupRequestToUser(SignupRequest signupRequest) {
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = validatedRoles(strRoles);
        user.setRoles(roles);
        user.setEnable((byte) 1);
        return user;
    }
}
