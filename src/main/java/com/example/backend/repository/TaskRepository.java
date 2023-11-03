package com.example.backend.repository;

import com.example.backend.model.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByIsDone(boolean isDone);

    List<Task> findTaskByUserId(Long userId);

    // Xóa tất cả các công việc đã hoàn thành
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Task t WHERE t.isDone = true AND t.user.id = :userId")
    void deleteDoneTaskByUserId(Long userId);

    @Query(value = "SELECT t FROM Task t where t.user.id =:id order by t.id desc ")
    Page<Task> getListByUserId(Long id, Pageable pageable);

    @Query(value = "SELECT t FROM Task t order by t.id desc")
    Page<Task> getAllTask(Pageable pageable);
}
