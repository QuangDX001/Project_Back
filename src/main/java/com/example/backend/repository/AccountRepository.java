package com.example.backend.repository;

import com.example.backend.model.Account;
import com.example.backend.payload.dto.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Admin on 10/25/2023
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//    @Query(value = "SELECT " +
//            "a.user_id as id," +
//            " u.username as username, " +
//            "a.balance as balance " +
//            "FROM account a " +
//            "join users u " +
//            "on a.user_id = u.id", nativeQuery = true)
//    List<IAccount> getUsernameAndBalance();

    @Query("SELECT NEW com.example.backend.payload.dto.AccountDTO(u.id, " +
            "u.username, a.balance) " +
            "FROM Account a " +
            "JOIN a.user u ")
    List<AccountDTO> getUsernameAndBalance2();
}
