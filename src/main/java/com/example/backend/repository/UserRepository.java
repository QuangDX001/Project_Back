package com.example.backend.repository;

import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Admin on 10/9/2023
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query("SELECT e FROM User e ORDER BY e.username DESC")
    List<User> findAllOrderByNameDesc();

    @Query(value = "SELECT * FROM users e ORDER BY e.username", nativeQuery = true)
    List<User> findAllOrderByNameAsc();
}
