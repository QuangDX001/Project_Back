package com.example.backend.repository;

import com.example.backend.model.SubTask;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Admin on 11/29/2023
 */
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE SubTask t SET t.position = :position WHERE t.id = :id")
    void updateTaskPosition(@Param("id") Long id, @Param("position") Integer position);

    @Modifying
    @Query("UPDATE SubTask t SET t.position = t.position + :increment WHERE t.task.id = :taskId AND t.id <> :id")
    void incrementPositions(@Param("taskId") Long taskId, @Param("increment") int increment, @Param("id") Long id);

    @Modifying
    @Query("UPDATE SubTask t SET t.position = t.position - 1 WHERE t.task.id = :taskId AND t.position > :position")
    void decrementPositions(@Param("taskId") Long taskId, @Param("position") int position);
}
