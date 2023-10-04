package com.example.backend.repository;

import com.example.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByIsDone(boolean isDone);

//    // Xóa tất cả các công việc đã hoàn thành
//    void deleteByIsDoneTrue();
//
//    // Xóa tất cả các công việc chưa hoàn thành
//    void deleteByIsDoneFalse();
}
