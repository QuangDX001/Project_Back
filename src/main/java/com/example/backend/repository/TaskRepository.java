package com.example.backend.repository;

import com.example.backend.model.Task;
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

    @Query(value = "SELECT t FROM Task t where t.user.id =:id")
    List<Task> getListByUserId(Long id);

    @Query("SELECT COALESCE(MIN(t.position), 0) FROM Task t WHERE t.user.id = :userId")
    int getMinPositionForUser(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Task t SET t.position = t.position + :increment WHERE t.user.id = :userId AND t.id <> :taskId")
    void incrementPositionsForUser(@Param("userId") Long userId, @Param("increment") int increment, @Param("taskId") Long taskId);

    @Modifying
    @Query("UPDATE Task t SET t.position = t.position - 1 WHERE t.user.id = :userId AND t.position > :position")
    void decrementPositionsForUser(@Param("userId") Long userId, @Param("position") int position);

    @Query(value = "SELECT t FROM Task t JOIN User u ON u.id = t.user.id")
    List<Task> getAllTask();

    @Query(value = "SELECT t FROM Task t where t.isDone = true and t.user.id =:id")
    List<Task> getDoneTaskByUserId(Long id);
}
