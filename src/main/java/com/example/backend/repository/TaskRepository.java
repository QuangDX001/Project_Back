package com.example.backend.repository;

import com.example.backend.model.Account;
import com.example.backend.model.Task;
import com.example.backend.payload.dto.account.AccountByTaskIdDTO;
import com.example.backend.payload.dto.account.AccountDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findTaskByUserId(Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Task t SET t.position = :position WHERE t.id = :id")
    void updateTaskPosition(@Param("id") Long id, @Param("position") Integer position);

    //return the first non-null value, else return 0
    @Query("SELECT COALESCE(MIN(t.position), 0) FROM Task t WHERE t.user.id = :userId")
    int getMinPositionForUser(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Task t SET t.position = t.position + :increment WHERE t.user.id = :userId AND t.id <> :taskId")
    void incrementPositionsForUser(@Param("userId") Long userId, @Param("increment") int increment, @Param("taskId") Long taskId);

    @Modifying
    @Query("UPDATE Task t SET t.position = t.position - 1 WHERE t.user.id = :userId AND t.position > :position")
    void decrementPositionsForUser(@Param("userId") Long userId, @Param("position") int position);

    @Query(value = "SELECT t FROM Task t where t.isDone = true and t.user.id =:id")
    List<Task> getDoneTaskByUserId(Long id);

    @Query(value = "SELECT t FROM Task t JOIN User u ON u.id = t.user.id")
    List<Task> getAllTask();

    @Query(value = "SELECT t FROM Task t where t.user.id =:id")
    List<Task> getListByUserId(Long id);

    @Query(value = "SELECT t from Task t left join fetch t.subTasks where t.user.id = :id")
    List<Task> getAllWithSubTaskByUserId(@Param("id") Long id);

    //@Query(value = "SELECT t from Task t left join fetch t.subTasks order by t.id desc ")
    @Query(value = "SELECT t FROM Task t")
    Page<Task> getAllTaskForMod(Pageable pageable);

    @Query("SELECT NEW com.example.backend.payload.dto.account.AccountByTaskIdDTO(u.id ,u.username, u.email, a.phone, a.address)" +
            "FROM Task t " +
            "JOIN t.user u " +
            "JOIN u.account a " +
            "WHERE t.id = :taskId")
    AccountByTaskIdDTO getAccountByTaskId(@Param("taskId") Long taskId);
}
